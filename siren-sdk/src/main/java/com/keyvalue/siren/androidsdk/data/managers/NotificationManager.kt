package com.keyvalue.siren.androidsdk.data.managers

import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback
import com.keyvalue.siren.androidsdk.data.repository.NotificationRepository
import com.keyvalue.siren.androidsdk.data.repository.NotificationRepositoryImplementation
import com.keyvalue.siren.androidsdk.data.state.AllNotificationState
import com.keyvalue.siren.androidsdk.data.state.MarkAsReadByIdState
import com.keyvalue.siren.androidsdk.data.state.NotificationUnViewedState
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONObject

class NotificationManager(baseURL: String) {
    private var service: NotificationRepository =
        NotificationRepositoryImplementation(baseURL)
    var notificationUnViewedState: MutableStateFlow<NotificationUnViewedState?> =
        MutableStateFlow(null)
    var allNotificationsState: MutableStateFlow<AllNotificationState?> =
        MutableStateFlow(null)
    var markAsReadByIdState: MutableStateFlow<MarkAsReadByIdState?> =
        MutableStateFlow(null)

    suspend fun fetchUnViewedNotificationsCount(
        userToken: String,
        recipientId: String,
    ) {
        service.fetchUnViewedNotificationsCount(
            userToken,
            recipientId,
            object : NetworkCallback {
                override suspend fun onResult(classObject: Any) {
                    val notificationUnViewedState =
                        NotificationUnViewedState(
                            notificationUnViewedResponse = classObject as UnViewedNotificationResponseData,
                            errorResponse = null,
                        )
                    this@NotificationManager.notificationUnViewedState.emit(
                        notificationUnViewedState,
                    )
                }

                override suspend fun onError(errorObject: JSONObject) {
                    val notificationUnViewedState =
                        NotificationUnViewedState(
                            notificationUnViewedResponse = null,
                            errorResponse = errorObject,
                        )
                    this@NotificationManager.notificationUnViewedState.emit(
                        notificationUnViewedState,
                    )
                }
            },
        )
    }

    suspend fun fetchAllNotifications(
        userToken: String,
        recipientId: String,
        page: Int? = null,
        size: Int? = null,
        start: String? = null,
        end: String? = null,
        isRead: Boolean? = null,
    ) {
        service.fetchAllNotifications(
            userToken = userToken,
            recipientId = recipientId,
            page = page,
            size = size,
            start = start,
            end = end,
            isRead = isRead,
            networkCallback =
                object : NetworkCallback {
                    override suspend fun onResult(classObject: Any) {
                        val allNotificationsState =
                            AllNotificationState(
                                allNotificationResponse = classObject as List<AllNotificationResponseData>,
                                errorResponse = null,
                            )
                        this@NotificationManager.allNotificationsState.emit(
                            allNotificationsState,
                        )
                    }

                    override suspend fun onError(errorObject: JSONObject) {
                        val allNotificationsState =
                            AllNotificationState(
                                allNotificationResponse = null,
                                errorResponse = errorObject,
                            )
                        this@NotificationManager.allNotificationsState.emit(
                            allNotificationsState,
                        )
                    }
                },
        )
    }

    suspend fun markAsReadById(
        userToken: String,
        recipientId: String,
        notificationId: String,
    ) {
        service.markAsReadById(
            userToken = userToken,
            recipientId = recipientId,
            notificationId = notificationId,
            networkCallback =
                object : NetworkCallback {
                    override suspend fun onResult(classObject: Any) {
                        val markAsReadByIdState =
                            MarkAsReadByIdState(
                                markAsReadByIdResponse = classObject as MarkAsReadByIdResponseData,
                                errorResponse = null,
                            )
                        this@NotificationManager.markAsReadByIdState.emit(
                            markAsReadByIdState,
                        )
                    }

                    override suspend fun onError(errorObject: JSONObject) {
                        val markAsReadByIdState =
                            MarkAsReadByIdState(
                                markAsReadByIdResponse = null,
                                errorResponse = errorObject,
                            )
                        this@NotificationManager.markAsReadByIdState.emit(
                            markAsReadByIdState,
                        )
                    }
                },
        )
    }
}
