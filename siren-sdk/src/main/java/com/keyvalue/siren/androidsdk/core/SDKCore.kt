package com.keyvalue.siren.androidsdk.core

import android.content.Context
import com.keyvalue.siren.androidsdk.AuthorizeUserAction
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.DataStatus
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponseData
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import com.keyvalue.siren.androidsdk.presenter.NotificationPresenter
import com.keyvalue.siren.androidsdk.utils.constants.TokenVerificationStatus
import org.json.JSONObject

abstract class SDKCore(var context: Context, var userToken: String, var recipientId: String) {
    var startDateState: String = ""
    private var notificationPresenter: NotificationPresenter =
        NotificationPresenter(context, userToken, recipientId)

    protected fun fetchUnViewedNotificationsCount(
        callback: (UnViewedNotificationResponseData?, JSONObject?, Boolean) -> Unit,
    ) {
        AuthorizeUserAction.authorizeUserAction { jsonError, authStatus ->
            if (jsonError.toString().isNotEmpty() && jsonError != null) {
                callback(null, jsonError, true)
            } else {
                if (authStatus == TokenVerificationStatus.SUCCESS) {
                    notificationPresenter.fetchUnViewedNotificationsCount(callback)
                }
            }
        }
    }

    protected fun markNotificationsAsViewedInner(
        startDate: String?,
        callback: (MarkAsViewedResponseData?, JSONObject?, Boolean) -> Unit,
    ) {
        AuthorizeUserAction.authorizeUserAction { jsonError, authStatus ->
            if (jsonError.toString().isNotEmpty() && jsonError != null) {
                callback(null, jsonError, true)
            } else {
                if (authStatus == TokenVerificationStatus.SUCCESS) {
                    val notificationPresenter = NotificationPresenter(context, userToken, recipientId)
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
        }
    }

    protected fun fetchAllNotifications(
        page: Int? = null,
        size: Int? = null,
        start: String? = null,
        end: String? = null,
        isRead: Boolean? = null,
        callback: (List<AllNotificationResponseData?>?, JSONObject?, Boolean) -> Unit,
    ) {
        AuthorizeUserAction.authorizeUserAction { jsonError, authStatus ->
            if (jsonError.toString().isNotEmpty() && jsonError != null) {
                callback(null, jsonError, true)
            } else {
                if (authStatus == TokenVerificationStatus.SUCCESS) {
                    val notificationPresenter = NotificationPresenter(context, userToken, recipientId)
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
        }
    }

    protected fun deleteNotificationsByDateInner(
        startDate: String?,
        callback: (DataStatus?, JSONObject?, Boolean) -> Unit,
    ) {
        AuthorizeUserAction.authorizeUserAction { jsonError, authStatus ->
            if (jsonError.toString().isNotEmpty() && jsonError != null) {
                callback(null, jsonError, true)
            } else {
                if (authStatus == TokenVerificationStatus.SUCCESS) {
                    val notificationPresenter = NotificationPresenter(context, userToken, recipientId)
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
        }
    }

    protected fun deleteNotificationInner(
        notificationId: String,
        callback: (DataStatus?, String?, JSONObject?, Boolean) -> Unit,
    ) {
        AuthorizeUserAction.authorizeUserAction { jsonError, authStatus ->
            if (jsonError.toString().isNotEmpty() && jsonError != null) {
                callback(null, "", jsonError, true)
            } else {
                if (authStatus == TokenVerificationStatus.SUCCESS) {
                    val notificationPresenter = NotificationPresenter(context, userToken, recipientId)
                    notificationPresenter.deleteNotificationById(
                        notificationId = notificationId,
                        callback = callback,
                    )
                }
            }
        }
    }
}
