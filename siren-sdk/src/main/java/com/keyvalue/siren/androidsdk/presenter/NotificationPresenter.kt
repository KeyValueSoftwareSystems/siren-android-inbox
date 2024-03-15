package com.keyvalue.siren.androidsdk.presenter

import com.keyvalue.siren.androidsdk.data.managers.NotificationManager
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.DataStatus
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class NotificationPresenter(
    private var userToken: String,
    private var recipientId: String,
) : BasePresenter() {
    private var notificationManager: NotificationManager? = null

    init {
        notificationManager = NotificationManager(baseURL)
    }

    fun fetchUnViewedNotificationsCount(callback: (UnViewedNotificationResponseData?, JSONObject?, Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationManager?.fetchUnViewedNotificationsCount(userToken, recipientId)
            notificationManager?.notificationUnViewedState?.collect { notificationUnViewedState ->
                if (notificationUnViewedState?.errorResponse == null) {
                    notificationUnViewedState?.notificationUnViewedResponse?.let { response ->
                        callback(
                            response,
                            null,
                            false,
                        )
                    }
                } else {
                    callback(
                        null,
                        notificationUnViewedState.errorResponse,
                        true,
                    )
                }
            }
        }
    }

    fun fetchAllNotifications(
        page: Int? = null,
        size: Int? = null,
        start: String? = null,
        end: String? = null,
        isRead: Boolean? = null,
        callback: (List<AllNotificationResponseData>, JSONObject?, Boolean) -> Unit,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationManager?.fetchAllNotifications(
                userToken = userToken,
                recipientId = recipientId,
                page = page,
                size = size,
                start = start,
                end = end,
                isRead = isRead,
            )
            notificationManager?.allNotificationsState?.collect { allNotificationState ->
                if (allNotificationState?.errorResponse == null) {
                    allNotificationState?.allNotificationResponse?.let { response ->
                        withContext(Dispatchers.Main) {
                            callback(
                                response,
                                null,
                                false,
                            )
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        callback(
                            emptyList(),
                            allNotificationState.errorResponse,
                            true,
                        )
                    }
                }
            }
        }
    }

    fun markAsReadById(
        callback: (data: MarkAsReadByIdResponseData?, error: JSONObject?, isError: Boolean) -> Unit,
        notificationId: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationManager?.markAsReadById(userToken, recipientId, notificationId)

            notificationManager?.markAsReadByIdState?.collect { markAsReadByIdState ->
                if (markAsReadByIdState?.errorResponse == null) {
                    markAsReadByIdState?.markAsReadByIdResponse?.let { response ->
                        withContext(Dispatchers.Main) {
                            callback(
                                response,
                                null,
                                false,
                            )
                        }
                    }
                } else {
                    callback(
                        null,
                        markAsReadByIdState.errorResponse,
                        true,
                    )
                }
            }
        }
    }

    fun markAllAsRead(
        callback: (data: DataStatus?, error: JSONObject?, isError: Boolean) -> Unit,
        startDate: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationManager?.markAllAsRead(userToken, recipientId, startDate)

            notificationManager?.markAllAsReadState?.collect { markAllAsReadState ->
                if (markAllAsReadState?.errorResponse == null) {
                    markAllAsReadState?.markAllAsReadResponse?.let { response ->
                        withContext(Dispatchers.Main) {
                            callback(
                                response,
                                null,
                                false,
                            )
                        }
                    }
                } else {
                    callback(
                        null,
                        markAllAsReadState.errorResponse,
                        true,
                    )
                }
            }
        }
    }
}
