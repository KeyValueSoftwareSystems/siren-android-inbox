package com.keyvalue.siren.androidsdk.helper

import android.content.Context
import androidx.compose.runtime.Composable
import com.keyvalue.siren.androidsdk.AuthorizeUserAction.authenticationStatus
import com.keyvalue.siren.androidsdk.AuthorizeUserAction.authorizeUserAction
import com.keyvalue.siren.androidsdk.core.SDKCoreUI
import com.keyvalue.siren.androidsdk.data.model.DataStatus
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
import com.keyvalue.siren.androidsdk.presenter.CorePresenter
import com.keyvalue.siren.androidsdk.presenter.NotificationPresenter
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_INVALID_CREDENTIALS
import com.keyvalue.siren.androidsdk.utils.constants.INVALID_CREDENTIALS
import com.keyvalue.siren.androidsdk.utils.constants.SdkState
import com.keyvalue.siren.androidsdk.utils.constants.SirenErrorTypes
import com.keyvalue.siren.androidsdk.utils.constants.TokenVerificationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class SirenSDKCore(
    context: Context,
    userToken: String,
    recipientId: String,
    private var errorCallback: ErrorCallback,
) : SirenSDKClient, SDKCoreUI(context, userToken, recipientId) {
    init {
        this.context = context
        initialize(userToken, recipientId)
        corePresenter = CorePresenter(userToken, recipientId)
    }

    private fun initialize(
        userToken: String,
        recipientId: String,
    ) {
        if (userToken.isEmpty() || recipientId.isEmpty()) {
            errorCallback.onError(
                JSONObject().put("type", SirenErrorTypes.CONFIG_ERROR)
                    .put("code", INVALID_CREDENTIALS).put("message", ERROR_MESSAGE_INVALID_CREDENTIALS),
            )
        } else {
            this.userToken = userToken
            this.recipientId = recipientId
            val authenticationPresenter =
                context?.let { AuthenticationPresenter(it, userToken, recipientId) }
            authenticationStatus = TokenVerificationStatus.PENDING
            authenticationPresenter?.verifyToken { boolean, _ ->
                authenticationStatus =
                    if (boolean) TokenVerificationStatus.SUCCESS else TokenVerificationStatus.FAILED
                authenticationState.emit(authenticationStatus)
            }
        }
    }

    override fun markAsReadById(
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

    private fun markAsReadByDateInner(
        startDate: String?,
        callback: (DataStatus?, JSONObject?, Boolean) -> Unit,
    ) {
        authorizeUserAction { jsonError, authStatus ->
            if (jsonError.toString().isNotEmpty()) {
                callback(null, jsonError, true)
            } else {
                if (authStatus == TokenVerificationStatus.SUCCESS) {
                    val notificationPresenter =
                        context?.let { NotificationPresenter(it, userToken, recipientId) }
                    if (startDate != null) {
                        notificationPresenter?.markAllAsRead(
                            startDate = startDate,
                            callback = callback,
                        )
                    } else {
                        notificationPresenter?.markAllAsRead(
                            startDate = startDateState,
                            callback = callback,
                        )
                    }
                }
            }
        }
    }

    override fun markAsReadByDate(
        untilDate: String?,
        callback: SirenAllNotificationUpdateCallback,
    ) {
        markAsReadByDateInner(untilDate) { responseData, jsonObject, isError ->
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

    override fun markAllAsViewed(
        untilDate: String?,
        callback: MarkAsViewedCallback,
    ) {
        markAllAsViewedInner(untilDate) { responseData, jsonObject, isError ->
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

    override fun deleteByDate(
        untilDate: String?,
        callback: SirenAllNotificationUpdateCallback,
    ) {
        deleteByDateInner(untilDate) { dataStatus, jsonObject, isError ->
            CoroutineScope(Dispatchers.Main).launch {
                if (isError && jsonObject != null) {
                    callback.onError(jsonObject)
                } else {
                    clearState.emit(DataStatus("SUCCESS"))
                    callback.onSuccess(dataStatus)
                }
            }
        }
    }

    override fun deleteById(
        id: String,
        callback: SirenAllNotificationUpdateCallback,
    ) {
        deleteByIdInner(id) { dataStatus, id, jsonObject, isError ->
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
        super.SirenCoreInboxIcon(props, callback)
    }

    @Composable
    override fun SirenInbox(
        props: SirenInboxProps,
        callback: SirenInboxCallback,
    ) {
        super.SirenCoreInbox(props, callback)
    }
}
