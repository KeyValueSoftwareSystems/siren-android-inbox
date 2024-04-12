package com.keyvalue.siren.androidsdk.core

import android.content.Context
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.keyvalue.siren.androidsdk.AuthorizeUserAction.authenticationStatus
import com.keyvalue.siren.androidsdk.R
import com.keyvalue.siren.androidsdk.core.elements.CircularLoader
import com.keyvalue.siren.androidsdk.core.elements.Header
import com.keyvalue.siren.androidsdk.core.elements.InboxEmptyScreen
import com.keyvalue.siren.androidsdk.core.elements.InboxErrorScreen
import com.keyvalue.siren.androidsdk.core.elements.NotificationCard
import com.keyvalue.siren.androidsdk.core.elements.RenderBadge
import com.keyvalue.siren.androidsdk.core.elements.SkeletonLoader
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.DataStatus
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponseData
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxIconCallback
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxIconProps
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxProps
import com.keyvalue.siren.androidsdk.presenter.CorePresenter
import com.keyvalue.siren.androidsdk.utils.SirenSDKUtils
import com.keyvalue.siren.androidsdk.utils.constants.DATA_FETCH_INTERVAL
import com.keyvalue.siren.androidsdk.utils.constants.DEFAULT_ITEMS_PER_FETCH
import com.keyvalue.siren.androidsdk.utils.constants.DEFAULT_WINDOW_TITLE
import com.keyvalue.siren.androidsdk.utils.constants.MAXIMUM_ITEMS_PER_FETCH
import com.keyvalue.siren.androidsdk.utils.constants.MAX_VERIFICATION_RETRY
import com.keyvalue.siren.androidsdk.utils.constants.SdkState
import com.keyvalue.siren.androidsdk.utils.constants.TOKEN_VERIFICATION_RETRY_INTERVAL
import com.keyvalue.siren.androidsdk.utils.constants.TokenVerificationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

abstract class SDKCoreUI(context: Context, userToken: String, recipientId: String) :
    SDKCore(context, userToken, recipientId) {
    private var unViewedNotificationTimer: Timer = Timer()
    private var allNotificationTimer: Timer = Timer()
    protected var corePresenter: CorePresenter? = null

    protected var clearState: MutableStateFlow<DataStatus> = MutableStateFlow(DataStatus(""))
    protected var markAsReadByIdState: MutableStateFlow<String> = MutableStateFlow("")
    protected var markAllAsReadState: MutableStateFlow<String> = MutableStateFlow("")

    protected var deleteByIdState: MutableStateFlow<String> = MutableStateFlow("")
    protected var sdkRestartState: MutableStateFlow<SdkState> =
        MutableStateFlow(SdkState.NOT_RESTART)
    protected var authenticationState: MutableStateFlow<TokenVerificationStatus?> =
        MutableStateFlow(TokenVerificationStatus.PENDING)
    protected var markAsViewedState: MutableStateFlow<MarkAsViewedResponseData?> =
        MutableStateFlow(null)

    @Composable
    fun SirenCoreInboxIcon(
        props: SirenInboxIconProps,
        callback: SirenInboxIconCallback,
    ) {
        var isNotificationFetchScheduled = false
        val styles =
            SirenSDKUtils.applyTheme(
                clientTheme = props.theme,
                customStyles = props.customStyles,
                isDarkMode = props.darkMode ?: false,
            )
        val badgeStyle = styles.badgeStyle

        val iconStyle = styles.notificationIcon

        var unViewedCount by remember {
            mutableLongStateOf(0)
        }

        var verificationRetryCount = 1
        val verificationRetryTimer: Timer = Timer()

        Box(
            modifier =
                Modifier.clickable(
                    enabled = !(props.disabled ?: false),
                    onClick = { callback.onClick() },
                ),
        ) {
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
            unViewedNotificationTimer.scheduleAtFixedRate(task, 1000, DATA_FETCH_INTERVAL)
        }

        fun fetchCount() {
            fetchUnViewedNotificationsCount { response, error, isError ->
                unViewedCount =
                    if (isError) {
                        error?.let { callback.onError(it) }
                        unViewedNotificationTimer.cancel()
                        0
                    } else {
                        if (!isNotificationFetchScheduled) {
                            val task =
                                object : TimerTask() {
                                    override fun run() {
                                        fetchCount()
                                    }
                                }
                            scheduleUnViewedNotificationsTimer(task)
                            isNotificationFetchScheduled = true
                        }
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
                CoroutineScope(Dispatchers.Main).launch {
                    sdkRestartState.emit(SdkState.RESTART)
                }
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SirenCoreInbox(
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

        val styles =
            SirenSDKUtils.applyTheme(
                clientTheme = props.theme,
                customStyles = props.customStyles,
                isDarkMode = props.darkMode ?: false,
            )

        val windowStyle = styles.window
        val windowHeaderStyle = styles.windowHeader
        val windowContainerStyle = styles.windowContainer
        val notificationCardStyle = styles.notificationCard
        val themeColors = styles.colors

        var verificationRetryCount = 1
        val verificationRetryTimer: Timer = Timer()
        val notificationsPerPage =
            if (props.itemsPerFetch == null) {
                DEFAULT_ITEMS_PER_FETCH
            } else {
                maxOf(0, if (props.itemsPerFetch > MAXIMUM_ITEMS_PER_FETCH) MAXIMUM_ITEMS_PER_FETCH else props.itemsPerFetch)
            }

        fun executeMarkAsViewed(startDate: String) {
            markNotificationsAsViewedInner(
                startDate,
            ) { _, _, _ ->
            }
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
                if (isError) {
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
                        val createdAtDateTime =
                            notification?.createdAt?.let { dateFormat.parse(it) }
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
                CoroutineScope(Dispatchers.Main).launch {
                    sdkRestartState.emit(SdkState.RESTART)
                    verificationRetryCount += 1
                    verificationRetryTimer.schedule(task, TOKEN_VERIFICATION_RETRY_INTERVAL)
                }
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
                if (props.inboxHeaderProps?.hideHeader != true) {
                    props.inboxHeaderProps?.customHeader?.let { it() } ?: Header(
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
                        hideClearAll = props.inboxHeaderProps?.hideClearAll ?: false,
                        themeColors = themeColors,
                        showBackButton = props.inboxHeaderProps?.showBackButton ?: false,
                        handleBackNavigation = props.inboxHeaderProps?.handleBackNavigation,
                        backButton = props.inboxHeaderProps?.backButton,
                        clearAllIconSize = styles.windowHeader.clearAllIconSize!!,
                    ) {
                        deleteNotificationsByDateInner(
                            startDate = null,
                        ) { dataStatus, jsonObject, isError ->
                            CoroutineScope(Dispatchers.Main).launch {
                                if (isError && jsonObject != null) {
                                    callback.onError(jsonObject)
                                } else {
                                    clearState.emit(DataStatus("SUCCESS"))
                                    showListEmptyState = true
                                }
                            }
                        }
                    }
                }

                if (isInitialListCall || isRetry.value || isRefreshing) {
                    props.customLoader?.let { it() } ?: SkeletonLoader(isDarkMode = props.darkMode)
                } else if (showListEmptyState) {
                    props.listEmptyComponent?.let { it() } ?: InboxEmptyScreen(
                        pullRefreshState = pullRefreshState,
                        backgroundColor = themeColors?.neutralColor!!,
                        titleColor = themeColors.textColor!!,
                        descriptionColor = Color(0xFF667185),
                        isDarkMode = props.darkMode ?: false,
                    )
                } else if (showListErrorState) {
                    props.customErrorWindow?.let { it() }
                        ?: InboxErrorScreen(
                            pullRefreshState = pullRefreshState,
                            backgroundColor = themeColors?.neutralColor!!,
                            titleColor = themeColors.textColor!!,
                            descriptionColor = Color(0xFF667185),
                            isDarkMode = props.darkMode ?: false,
                        )
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
                                    notification = notificationData,
                                    cardProps = props.cardProps,
                                    notificationCardStyle,
                                    onCardClick = {
                                        callback.onCardClick(it)
                                    },
                                    deleteNotificationCallback = {
                                        notificationData?.id?.let {
                                            deleteNotificationInner(
                                                it,
                                            ) { dataStatus, id, jsonObject, isError ->
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    if (isError && jsonObject != null) {
                                                        callback.onError(jsonObject)
                                                    } else {
                                                        deleteByIdState.emit(it)
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    themeColors = themeColors,
                                    darkMode = props.darkMode ?: false,
                                    defaultCardClickCallback = {
                                        // Add callback
                                    },
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
