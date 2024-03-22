package com.keyvalue.siren.androidsdk.helper

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.keyvalue.siren.androidsdk.R
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.DataStatus
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponseData
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import com.keyvalue.siren.androidsdk.helper.client.CombinedBadgeThemeProps
import com.keyvalue.siren.androidsdk.helper.client.NotificationCardProps
import com.keyvalue.siren.androidsdk.helper.client.SirenSDKClient
import com.keyvalue.siren.androidsdk.helper.client.callbacks.ErrorCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.MarkAsReadByIdCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.MarkAsViewedCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenAllNotificationUpdateCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxIconCallback
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxIconProps
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxProps
import com.keyvalue.siren.androidsdk.presenter.AuthenticationPresenter
import com.keyvalue.siren.androidsdk.presenter.NotificationPresenter
import com.keyvalue.siren.androidsdk.utils.SirenSDKUtils
import com.keyvalue.siren.androidsdk.utils.SirenSDKUtils.applyTheme
import com.keyvalue.siren.androidsdk.utils.constants.DATA_FETCH_INTERVAL
import com.keyvalue.siren.androidsdk.utils.constants.DEFAULT_WINDOW_TITLE
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_INVALID_RECIPIENT_ID
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_INVALID_TOKEN
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_TOKEN_VERIFICATION_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.INVALID_RECIPIENT_ID
import com.keyvalue.siren.androidsdk.utils.constants.INVALID_TOKEN
import com.keyvalue.siren.androidsdk.utils.constants.MAX_VERIFICATION_RETRY
import com.keyvalue.siren.androidsdk.utils.constants.SdkState
import com.keyvalue.siren.androidsdk.utils.constants.SirenErrorTypes
import com.keyvalue.siren.androidsdk.utils.constants.TOKEN_VERIFICATION_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.TOKEN_VERIFICATION_RETRY_INTERVAL
import com.keyvalue.siren.androidsdk.utils.constants.TokenVerificationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class SirenSDKCore(
    _userToken: String,
    _recipientId: String,
    private var errorCallback: ErrorCallback,
) : SirenSDKClient, SDKCoreUI() {
    private var unViewedNotificationTimer: Timer = Timer()
    private var authenticationStatus: TokenVerificationStatus = TokenVerificationStatus.FAILED
    private var allNotificationTimer: Timer = Timer()
    private var userToken: String = ""
    private var recipientId: String = ""
    private var startDateState: String = ""

    private var authenticationState: MutableStateFlow<TokenVerificationStatus?> =
        MutableStateFlow(TokenVerificationStatus.PENDING)
    private var clearState: MutableStateFlow<DataStatus> = MutableStateFlow(DataStatus(""))
    private var markAsReadByIdState: MutableStateFlow<String> = MutableStateFlow("")
    private var markAllAsReadState: MutableStateFlow<String> = MutableStateFlow("")
    private var markAsViewedState: MutableStateFlow<MarkAsViewedResponseData?> =
        MutableStateFlow(null)
    private var deleteByIdState: MutableStateFlow<String> = MutableStateFlow("")
    private var sdkRestartState: MutableStateFlow<SdkState> = MutableStateFlow(SdkState.NOT_RESTART)

    init {
        initialize(_userToken, _recipientId)
    }

    private fun initialize(
        _userToken: String,
        _recipientId: String,
    ) {
        if (_userToken.isEmpty()) {
            errorCallback.onError(
                JSONObject().put("type", SirenErrorTypes.CONFIG_ERROR)
                    .put("code", INVALID_TOKEN).put("message", ERROR_MESSAGE_INVALID_TOKEN),
            )
        } else {
            userToken = _userToken
        }
        if (_recipientId.isEmpty()) {
            errorCallback.onError(
                JSONObject().put("type", SirenErrorTypes.CONFIG_ERROR)
                    .put("code", INVALID_RECIPIENT_ID)
                    .put("message", ERROR_MESSAGE_INVALID_RECIPIENT_ID),
            )
        } else {
            recipientId = _recipientId
        }
        if (userToken.isNotEmpty() && recipientId.isNotEmpty()) {
            val authenticationPresenter =
                AuthenticationPresenter(userToken, recipientId)
            authenticationStatus = TokenVerificationStatus.PENDING
            authenticationPresenter.verifyToken { boolean, _ ->
                authenticationStatus =
                    if (boolean) TokenVerificationStatus.SUCCESS else TokenVerificationStatus.FAILED
                authenticationState.emit(authenticationStatus)
            }
        }
    }

    private fun authorizeUserAction(callback: (error: JSONObject?) -> Unit): TokenVerificationStatus {
        if (authenticationStatus === TokenVerificationStatus.FAILED) {
            callback(
                JSONObject().put("type", SirenErrorTypes.ERROR)
                    .put("code", TOKEN_VERIFICATION_FAILED)
                    .put("message", ERROR_MESSAGE_TOKEN_VERIFICATION_FAILED),
            )
        }
        return authenticationStatus
    }

    private fun fetchUnViewedNotificationsCount(
        callback: (UnViewedNotificationResponseData?, JSONObject?, Boolean) -> Unit,
    ) {
        val authStatus =
            authorizeUserAction { jsonError ->
                if (jsonError.toString().isNotEmpty()) {
                    callback(null, jsonError, true)
                }
            }
        if (authStatus == TokenVerificationStatus.SUCCESS) {
            val notificationUnViewedPresenter = NotificationPresenter(userToken, recipientId)
            notificationUnViewedPresenter.fetchUnViewedNotificationsCount(callback)
        }
    }

    private fun fetchAllNotifications(
        page: Int? = null,
        size: Int? = null,
        start: String? = null,
        end: String? = null,
        isRead: Boolean? = null,
        callback: (List<AllNotificationResponseData?>?, JSONObject?, Boolean) -> Unit,
    ) {
        val authStatus =
            authorizeUserAction { jsonError ->
                if (jsonError.toString().isNotEmpty()) {
                    callback(null, jsonError, true)
                }
            }
        if (authStatus == TokenVerificationStatus.SUCCESS) {
            val notificationPresenter = NotificationPresenter(userToken, recipientId)
            notificationPresenter.fetchAllNotifications(
                page = page,
                size = size,
                start = start,
                end = end,
                isRead = isRead,
                callback,
            )
        }
    }

    private fun markAsReadInner(
        notificationId: String,
        callback: (MarkAsReadByIdResponseData?, JSONObject?, Boolean) -> Unit,
    ) {
        val authStatus =
            authorizeUserAction { jsonError ->
                if (jsonError.toString().isNotEmpty()) {
                    callback(null, jsonError, true)
                }
            }
        if (authStatus == TokenVerificationStatus.SUCCESS) {
            val notificationPresenter = NotificationPresenter(userToken, recipientId)
            notificationPresenter.markAsReadById(
                notificationId = notificationId,
                callback = callback,
            )
        }
    }

    override fun markAsRead(
        notificationId: String,
        callback: MarkAsReadByIdCallback,
    ) {
        markAsReadInner(notificationId) { responseData, jsonObject, isError ->
            CoroutineScope(Dispatchers.Main).launch {
                if (isError && jsonObject != null) {
                    callback.onError(jsonObject)
                } else {
                    if (responseData?.id?.isNotEmpty() == true) {
                        markAsReadByIdState.emit(responseData.id)
                        callback.onSuccess(responseData)
                    }
                }
            }
        }
    }

    private fun markNotificationsAsReadByDateInner(
        startDate: String?,
        callback: (DataStatus?, JSONObject?, Boolean) -> Unit,
    ) {
        val authStatus =
            authorizeUserAction { jsonError ->
                if (jsonError.toString().isNotEmpty()) {
                    callback(null, jsonError, true)
                }
            }
        if (authStatus == TokenVerificationStatus.SUCCESS) {
            val notificationPresenter = NotificationPresenter(userToken, recipientId)
            if (startDate != null) {
                notificationPresenter.markAllAsRead(
                    startDate = startDate,
                    callback = callback,
                )
            } else {
                notificationPresenter.markAllAsRead(
                    startDate = startDateState,
                    callback = callback,
                )
            }
        }
    }

    override fun markNotificationsAsReadByDate(
        untilDate: String?,
        callback: SirenAllNotificationUpdateCallback,
    ) {
        markNotificationsAsReadByDateInner(untilDate) { responseData, jsonObject, isError ->
            CoroutineScope(Dispatchers.Main).launch {
                if (isError && jsonObject != null) {
                    callback.onError(jsonObject)
                } else {
                    if (untilDate != null) {
                        markAllAsReadState.emit(untilDate)
                    } else {
                        markAllAsReadState.emit(startDateState)
                    }
                    callback.onSuccess(responseData)
                }
            }
        }
    }

    private fun markNotificationsAsViewedInner(
        startDate: String?,
        callback: (MarkAsViewedResponseData?, JSONObject?, Boolean) -> Unit,
    ) {
        val authStatus =
            authorizeUserAction { jsonError ->
                if (jsonError.toString().isNotEmpty()) {
                    callback(null, jsonError, true)
                }
            }
        if (authStatus == TokenVerificationStatus.SUCCESS) {
            val notificationPresenter = NotificationPresenter(userToken, recipientId)
            if (startDate != null) {
                notificationPresenter.markAsViewed(
                    startDate = startDate,
                    callback = callback,
                )
            } else {
                notificationPresenter.markAsViewed(
                    startDate = startDateState,
                    callback = callback,
                )
            }
        }
    }

    override fun markNotificationsAsViewed(
        untilDate: String?,
        callback: MarkAsViewedCallback,
    ) {
        markNotificationsAsViewedInner(untilDate) { responseData, jsonObject, isError ->
            CoroutineScope(Dispatchers.Main).launch {
                if (isError && jsonObject != null) {
                    callback.onError(jsonObject)
                } else if (responseData !== null) {
                    markAsViewedState.emit(responseData)
                    callback.onSuccess(responseData)
                }
            }
        }
    }

    private fun deleteNotificationsByDateInner(
        startDate: String?,
        callback: (DataStatus?, JSONObject?, Boolean) -> Unit,
    ) {
        val authStatus =
            authorizeUserAction { jsonError ->
                if (jsonError.toString().isNotEmpty()) {
                    callback(null, jsonError, true)
                }
            }
        if (authStatus == TokenVerificationStatus.SUCCESS) {
            val notificationPresenter = NotificationPresenter(userToken, recipientId)
            if (startDate != null) {
                notificationPresenter.clearAllNotifications(
                    startDate = startDate,
                    callback = callback,
                )
            } else {
                notificationPresenter.clearAllNotifications(
                    startDate = startDateState,
                    callback = callback,
                )
            }
        }
    }

    override fun deleteNotificationsByDate(
        untilDate: String?,
        callback: SirenAllNotificationUpdateCallback,
    ) {
        deleteNotificationsByDateInner(untilDate) { dataStatus, jsonObject, isError ->
            CoroutineScope(Dispatchers.Main).launch {
                clearState.emit(DataStatus("SUCCESS"))
                if (isError && jsonObject != null) {
                    callback.onError(jsonObject)
                } else {
                    callback.onSuccess(dataStatus)
                }
            }
        }
    }

    private fun deleteNotificationInner(
        notificationId: String,
        callback: (DataStatus?, String?, JSONObject?, Boolean) -> Unit,
    ) {
        val authStatus =
            authorizeUserAction { jsonError ->
                if (jsonError.toString().isNotEmpty()) {
                    callback(null, "", jsonError, true)
                }
            }
        if (authStatus == TokenVerificationStatus.SUCCESS) {
            val notificationPresenter = NotificationPresenter(userToken, recipientId)
            notificationPresenter.deleteNotificationById(
                notificationId = notificationId,
                callback = callback,
            )
        }
    }

    override fun deleteNotification(
        notificationId: String,
        callback: SirenAllNotificationUpdateCallback,
    ) {
        deleteNotificationInner(notificationId) { dataStatus, id, jsonObject, isError ->
            CoroutineScope(Dispatchers.Main).launch {
                deleteByIdState.emit(id ?: "")
                if (isError && jsonObject != null) {
                    callback.onError(jsonObject)
                } else {
                    callback.onSuccess(dataStatus)
                }
            }
        }
    }

    override fun updateToken(
        userToken: String,
        recipientId: String,
    ) {
        initialize(userToken, recipientId)
        CoroutineScope(Dispatchers.Main).launch {
            sdkRestartState.emit(SdkState.RESTART)
        }
    }

    @Composable
    override fun SirenInboxIcon(
        props: SirenInboxIconProps,
        callback: SirenInboxIconCallback,
    ) {
        val styles = applyTheme(clientTheme = props.theme, customStyles = props.customStyles, isDarkMode = props.darkMode ?: false)
        val badgeStyle = styles.badgeStyle

        val iconStyle = styles.notificationIcon

        var unViewedCount by remember {
            mutableLongStateOf(0)
        }

        var verificationRetryCount = 1
        val verificationRetryTimer: Timer = Timer()

        Box(modifier = Modifier.clickable(enabled = !(props.disabled ?: false), onClick = { callback.onClick() })) {
            if (props.notificationIcon !== null) {
                props.notificationIcon.also {
                    it()
                }
            } else {
                Icon(
                    painter = painterResource(id = if (props.darkMode == true) R.drawable.bell_dark else R.drawable.bell_light),
                    contentDescription = "notification icon with unread notifications indicator",
                    modifier = Modifier.size(iconStyle?.size!!),
                )
            }

            // Render badge with unViewedCount if count is greater than 0
            if (unViewedCount > 0) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .offset(
                                x = (-(styles.badgeStyle?.right ?: 0)).dp,
                                y = (styles.badgeStyle?.top ?: 0).dp,
                            )
                            .zIndex(1f),
                ) {
                    RenderBadge(unViewedCount, badgeStyle)
                }
            } else {
                Box(modifier = Modifier)
            }
        }

        fun scheduleUnViewedNotificationsTimer(task: TimerTask) {
            unViewedNotificationTimer.cancel()
            unViewedNotificationTimer = Timer()
            unViewedNotificationTimer.schedule(task, DATA_FETCH_INTERVAL)
        }

        fun fetchCount() {
            fetchUnViewedNotificationsCount { response, error, isError ->
                unViewedCount =
                    if (isError) {
                        error?.let { callback.onError(it) }
                        0
                    } else {
                        val task =
                            object : TimerTask() {
                                override fun run() {
                                    fetchCount()
                                }
                            }
                        scheduleUnViewedNotificationsTimer(task)
                        response?.totalUnviewed ?: 0
                    }
            }
        }

        sdkRestartState.collectAsState().apply {
            if (this.value == SdkState.RESTART) {
                unViewedNotificationTimer.cancel()
                unViewedNotificationTimer = Timer()
                fetchCount()
            }
        }

        fun retryTokenVerification() {
            val task =
                object : TimerTask() {
                    override fun run() {
                        retryTokenVerification()
                    }
                }
            if (authenticationStatus == TokenVerificationStatus.FAILED && verificationRetryCount <= MAX_VERIFICATION_RETRY) {
                initialize(userToken, recipientId)
                verificationRetryCount += 1
                verificationRetryTimer.schedule(task, TOKEN_VERIFICATION_RETRY_INTERVAL)
            }
            if (authenticationStatus == TokenVerificationStatus.SUCCESS) {
                fetchCount()
            }
        }

        DisposableEffect(Unit) {
            if (authenticationStatus == TokenVerificationStatus.FAILED) {
                retryTokenVerification()
            } else {
                fetchCount()
            }
            onDispose {
                unViewedNotificationTimer.cancel()
            }
        }

        authenticationState.collectAsState().apply {
            if (authenticationStatus == TokenVerificationStatus.PENDING) {
                fetchCount()
            }
        }

        markAsViewedState.collectAsState().apply {
            if (this.value !== null) {
                fetchCount()
            }
        }
    }

    @Composable
    fun RenderBadge(
        unViewedCount: Long,
        badgeStyle: CombinedBadgeThemeProps?,
    ) {
        Box(
            modifier =
                Modifier
                    .size(badgeStyle?.size!!)
                    .background(
                        color = badgeStyle.color!!,
                        shape = badgeStyle.borderShape!!,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (unViewedCount > 99) "99+" else unViewedCount.toString(),
                color = badgeStyle.textColor!!,
                fontSize = badgeStyle.textSize!!,
                textAlign = TextAlign.Center,
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun SirenInbox(
        props: SirenInboxProps,
        callback: SirenInboxCallback,
    ) {
        var notificationListState by remember {
            mutableStateOf<List<AllNotificationResponseData?>>(
                emptyList(),
            )
        }
        var isInitialListCall by remember {
            mutableStateOf(true)
        }
        var showListErrorState by remember {
            mutableStateOf(false)
        }
        var showListEmptyState by remember {
            mutableStateOf(false)
        }
        val isLoading = remember { mutableStateOf(false) }
        val isRetry = remember { mutableStateOf(false) }
        val listState = rememberLazyListState()
        val endTime = remember { mutableStateOf("") }
        val startTime = remember { mutableStateOf("") }
        var isEndReached by remember {
            mutableStateOf(false)
        }
        var isRefreshing by remember {
            mutableStateOf(false)
        }

        val styles = applyTheme(clientTheme = props.theme, customStyles = props.customStyles, isDarkMode = props.darkMode ?: false)

        val windowStyle = styles.window
        val windowHeaderStyle = styles.windowHeader
        val windowContainerStyle = styles.windowContainer
        val notificationCardStyle = styles.notificationCard
        val themeColors = styles.colors

        var verificationRetryCount = 1
        val verificationRetryTimer: Timer = Timer()
        val notificationsPerPage =
            if (props.itemsPerFetch == null) {
                20
            } else if (props.itemsPerFetch > 50) {
                50
            } else {
                props.itemsPerFetch
            }

        fun executeMarkAsViewed(startDate: String) {
            markNotificationsAsViewed(
                startDate,
                object : MarkAsViewedCallback {
                    override fun onSuccess(responseData: MarkAsViewedResponseData) {}

                    override fun onError(jsonObject: JSONObject) {}
                },
            )
        }

        fun scheduleFetchNotificationsTimer(task: TimerTask) {
            allNotificationTimer.cancel()
            allNotificationTimer = Timer()
            allNotificationTimer.schedule(task, DATA_FETCH_INTERVAL)
        }

        fun fetchNotifications() {
            val task =
                object : TimerTask() {
                    override fun run() {
                        fetchNotifications()
                    }
                }
            if (notificationListState.isNotEmpty()) {
                startTime.value = notificationListState[0]?.createdAt.toString()
            }

            fetchAllNotifications(
                size = notificationsPerPage,
                start = if (startTime.value.isNotEmpty()) SirenSDKUtils.addOneMillisecond(startTime.value) else null,
            ) { response, error, isError ->
                if (sdkRestartState.value == SdkState.RESTART) {
                    sdkRestartState.value = SdkState.NOT_RESTART
                }
                isRefreshing = false
                isInitialListCall = false
                isRetry.value = false
                if (isError && notificationListState.isEmpty()) {
                    error?.let { callback.onError(it) }
                    notificationListState = notificationListState.ifEmpty { emptyList() }
                    showListErrorState = true
                } else {
                    showListErrorState = false
                    if (!response.isNullOrEmpty()) {
                        response[0]?.let {
                            executeMarkAsViewed(it.createdAt)
                        }
                        notificationListState = response + notificationListState
                    }
                    if (notificationListState.isEmpty()) {
                        showListEmptyState = true
                    } else {
                        showListEmptyState = false
                        endTime.value =
                            notificationListState[notificationListState.size - 1]?.createdAt.toString()
                        startDateState = notificationListState[0]?.createdAt.toString()
                    }
                    scheduleFetchNotificationsTimer(task)
                }
            }
        }

        sdkRestartState.collectAsState().apply {
            if (this.value == SdkState.RESTART) {
                allNotificationTimer.cancel()
                allNotificationTimer = Timer()
                notificationListState = emptyList()
                startTime.value = ""
                isEndReached = false
                isInitialListCall = true
                fetchNotifications()
            }
        }

        val enableClearAllButton =
            notificationListState.isNotEmpty() && !showListEmptyState && !showListErrorState &&
                !isLoading.value && !isRefreshing && !isRetry.value

        fun retryFetch() {
            isRetry.value = true
            fetchNotifications()
        }

        authenticationState.collectAsState().apply {
            if (this.value == TokenVerificationStatus.FAILED) {
                showListErrorState = true
            }
        }
        clearState.collectAsState().apply {
            if (this.value.status.isNotBlank()) {
                clearState.value = DataStatus("")
                notificationListState = emptyList()
                startTime.value = ""
            }
        }

        deleteByIdState.collectAsState().apply {
            if (this.value.isNotEmpty()) {
                var position: Int
                notificationListState.mapIndexed { index, responseData ->
                    if (responseData?.id == this.value) {
                        position = index
                        notificationListState = notificationListState.subList(
                            0, if (position == 0) 0 else position,
                        ) +
                            notificationListState.subList(
                                position + 1, notificationListState.size,
                            )
                        if (notificationListState.isEmpty()) {
                            showListEmptyState = true
                        }
                    }
                }
            }
        }

        markAsReadByIdState.collectAsState().apply {
            val id = this.value
            if (id.isNotEmpty()) {
                notificationListState =
                    notificationListState.map { notification ->
                        if (notification?.id == id) {
                            notification.copy(isRead = true)
                        } else {
                            notification
                        }
                    }
            }
        }

        markAllAsReadState.collectAsState().apply {
            val startDate = this.value
            if (startDate.isNotEmpty()) {
                notificationListState =
                    notificationListState.map { notification ->
                        val dateFormat =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                        val startDateTime = dateFormat.parse(startDate)?.time
                        val createdAtDateTime = notification?.createdAt?.let { dateFormat.parse(it) }
                        if (startDateTime !== null && createdAtDateTime !== null && createdAtDateTime.time <= startDateTime) {
                            notification.copy(isRead = true)
                        } else {
                            notification
                        }
                    }
            }
        }

        fun retryTokenVerification() {
            val task =
                object : TimerTask() {
                    override fun run() {
                        retryTokenVerification()
                    }
                }
            if (authenticationStatus == TokenVerificationStatus.FAILED && verificationRetryCount <= MAX_VERIFICATION_RETRY) {
                initialize(userToken, recipientId)
                verificationRetryCount += 1
                verificationRetryTimer.schedule(task, TOKEN_VERIFICATION_RETRY_INTERVAL)
            }
            if (authenticationStatus == TokenVerificationStatus.SUCCESS) {
                fetchNotifications()
            }
        }

        DisposableEffect(Unit) {
            if (authenticationStatus == TokenVerificationStatus.FAILED) {
                isInitialListCall = false
                retryTokenVerification()
                showListErrorState = true
            } else {
                fetchNotifications()
            }
            onDispose {
                allNotificationTimer.cancel()
            }
        }

        val pullRefreshState =
            rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
                notificationListState = emptyList()
                startTime.value = ""
                isEndReached = false
                isRefreshing = true
                fetchNotifications()
            })

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.filterNotNull()
                .distinctUntilChanged().collectLatest { index ->
                    val isLastItemVisible = index >= (notificationListState.size - 1)
                    if (notificationListState.size < 10) {
                        isEndReached = true
                    }
                    val hasMoreData = endTime.value.isNotBlank() && !isEndReached
                    if (!isLoading.value && isLastItemVisible && hasMoreData) {
                        isLoading.value = true
                        fetchAllNotifications(
                            size = notificationsPerPage,
                            end = SirenSDKUtils.reduceOneMillisecond(endTime.value),
                        ) { response, _, _ ->
                            if (response?.isEmpty() == false) {
                                if (response.first()?.id == notificationListState.last()?.id) {
                                    isEndReached =
                                        true
                                } else {
                                    if (response.size < 10) isEndReached = true
                                    notificationListState = notificationListState + response
                                    if (notificationListState.isNotEmpty()) {
                                        endTime.value =
                                            SirenSDKUtils.reduceOneMillisecond(response.last()?.createdAt.toString())
                                    }
                                }
                            }

                            isLoading.value = false
                        }
                    }
                }
        }

        Box(modifier = Modifier) {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier =
                    Modifier
                        .zIndex(1f)
                        .align(Alignment.TopCenter),
            )
            Column(
                modifier =
                    Modifier
                        .height(windowStyle?.height!!)
                        .width(windowStyle.width!!)
                        .background(windowContainerStyle?.background!!)
                        .padding(windowContainerStyle.padding!!),
            ) {
                if (props.hideHeader == false) {
                    props.customHeader?.let { it() } ?: Header(
                        title = props.title ?: DEFAULT_WINDOW_TITLE,
                        titleColor = windowHeaderStyle?.titleColor!!,
                        titleFontSize = windowHeaderStyle.titleSize!!,
                        titleFontWeight = windowHeaderStyle.titleFontWeight!!,
                        headerActionColor = windowHeaderStyle.headerActionColor!!,
                        backgroundColor = windowHeaderStyle.background!!,
                        height = windowHeaderStyle.height!!,
                        enableClearAll = enableClearAllButton,
                        titlePadding = windowHeaderStyle.titlePadding!!,
                        borderBottomColor = windowHeaderStyle.borderColor!!,
                        hideClearAll = props.hideClearAll ?: false,
                        themeColors = themeColors,
                        clearAllIconSize = styles.windowHeader.clearAllIconSize!!,
                    ) {
                        deleteNotificationsByDate(
                            untilDate = null,
                            callback =
                                object : SirenAllNotificationUpdateCallback {
                                    override fun onSuccess(dataStatus: DataStatus?) {
                                        showListEmptyState = true
                                    }

                                    override fun onError(jsonObject: JSONObject) {
                                        callback.onError(jsonObject)
                                    }
                                },
                        )
                    }
                }

                if (isInitialListCall || isRetry.value || isRefreshing) {
                    props.customLoader?.let { it() }
                        ?: Box(modifier = Modifier.weight(1f)) {
                            SkeletonLoader(isDarkMode = props.darkMode)
                        }
                } else if (showListEmptyState) {
                    props.listEmptyComponent?.let { it() }
                        ?: Box(modifier = Modifier.weight(1f)) {
                            InboxEmptyState(
                                pullRefreshState = pullRefreshState,
                                backgroundColor = themeColors?.neutralColor!!,
                                titleColor = themeColors.textColor!!,
                                descriptionColor = Color(0xFF667185),
                                isDarkMode = props.darkMode ?: false,
                            )
                        }
                } else if (showListErrorState) {
                    props.customErrorWindow?.let { it() }
                        ?: Box(modifier = Modifier.weight(1f)) {
                            InboxErrorState(
                                pullRefreshState = pullRefreshState,
                                backgroundColor = themeColors?.neutralColor!!,
                                titleColor = themeColors.textColor!!,
                                descriptionColor = Color(0xFF667185),
                                isDarkMode = props.darkMode ?: false,
                            )
                        }
                } else {
                    LazyColumn(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .pullRefresh(pullRefreshState),
                        state = listState,
                    ) {
                        items(notificationListState) { notificationData ->
                            Box {
                                props.customNotificationCard?.let {
                                    if (notificationData != null) {
                                        it(notificationData)
                                    }
                                } ?: NotificationCard(
                                    NotificationCardProps(
                                        notification = notificationData,
                                        cardProps = props.cardProps,
                                    ),
                                    notificationCardStyle,
                                    onCardClick = {
                                        callback.onCardClick(it)
                                    },
                                    deleteNotificationCallback = {
                                        notificationData?.id?.let {
                                            deleteNotification(
                                                it,
                                                object : SirenAllNotificationUpdateCallback {
                                                    override fun onSuccess(dataStatus: DataStatus?) {
                                                        // Delete was successful.
                                                    }

                                                    override fun onError(jsonObject: JSONObject) {
                                                        callback.onError(jsonObject)
                                                    }
                                                },
                                            )
                                        }
                                    },
                                    themeColors = themeColors,
                                    darkMode = props.darkMode ?: false,
                                )
                            }
                        }
                        item {
                            if (isLoading.value) {
                                CircularLoader(color = themeColors?.infiniteLoader)
                            }
                        }
                    }
                }
                props.customFooter?.let { it() }
            }
        }
    }
}
