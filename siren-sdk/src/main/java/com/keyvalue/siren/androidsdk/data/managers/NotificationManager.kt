package com.keyvalue.siren.androidsdk.data.managers

import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.DataStatus
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponseData
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback
import com.keyvalue.siren.androidsdk.data.repository.NotificationRepository
import com.keyvalue.siren.androidsdk.data.repository.NotificationRepositoryImplementation
import com.keyvalue.siren.androidsdk.data.state.AllNotificationState
import com.keyvalue.siren.androidsdk.data.state.ClearAllNotificationsState
import com.keyvalue.siren.androidsdk.data.state.DeleteNotificationByIdState
import com.keyvalue.siren.androidsdk.data.state.MarkAllAsReadState
import com.keyvalue.siren.androidsdk.data.state.MarkAsReadByIdState
import com.keyvalue.siren.androidsdk.data.state.MarkAsViewedViewedState
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
    var markAllAsReadState: MutableStateFlow<MarkAllAsReadState?> =
        MutableStateFlow(null)
    var markAsViewedState: MutableStateFlow<MarkAsViewedViewedState?> =
        MutableStateFlow(null)
    var deleteNotificationByIdState: MutableStateFlow<DeleteNotificationByIdState?> =
        MutableStateFlow(null)
    var clearAllNotificationsState: MutableStateFlow<ClearAllNotificationsState?> =
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

    suspend fun markAllAsRead(
        userToken: String,
        recipientId: String,
        startDate: String,
    ) {
        service.markAllAsRead(
            userToken = userToken,
            recipientId = recipientId,
            startDate = startDate,
            networkCallback =
                object : NetworkCallback {
                    override suspend fun onResult(classObject: Any) {
                        val markAllAsReadState =
                            MarkAllAsReadState(
                                markAllAsReadResponse = classObject as DataStatus,
                                errorResponse = null,
                            )
                        this@NotificationManager.markAllAsReadState.emit(
                            markAllAsReadState,
                        )
                    }

                    override suspend fun onError(errorObject: JSONObject) {
                        val markAllAsReadState =
                            MarkAllAsReadState(
                                markAllAsReadResponse = null,
                                errorResponse = errorObject,
                            )
                        this@NotificationManager.markAllAsReadState.emit(
                            markAllAsReadState,
                        )
                    }
                },
        )
    }

    suspend fun markAsViewed(
        userToken: String,
        recipientId: String,
        startDate: String,
    ) {
        service.markAsViewed(
            userToken = userToken,
            recipientId = recipientId,
            startDate = startDate,
            networkCallback =
                object : NetworkCallback {
                    override suspend fun onResult(classObject: Any) {
                        val markAsViewedViewedState =
                            MarkAsViewedViewedState(
                                markAsViewedResponse = classObject as MarkAsViewedResponseData,
                                errorResponse = null,
                            )
                        this@NotificationManager.markAsViewedState.emit(
                            markAsViewedViewedState,
                        )
                    }

                    override suspend fun onError(errorObject: JSONObject) {
                        val markAsViewedViewedState =
                            MarkAsViewedViewedState(
                                markAsViewedResponse = null,
                                errorResponse = errorObject,
                            )
                        this@NotificationManager.markAsViewedState.emit(
                            markAsViewedViewedState,
                        )
                    }
                },
        )
    }

    suspend fun deleteNotificationById(
        userToken: String,
        recipientId: String,
        notificationId: String,
    ) {
        service.deleteNotificationById(
            userToken = userToken,
            recipientId = recipientId,
            notificationId = notificationId,
            networkCallback =
                object : NetworkCallback {
                    override suspend fun onResult(classObject: Any) {
                        val deleteNotificationByIdState =
                            DeleteNotificationByIdState(
                                deleteStatus = classObject as DataStatus,
                                deleteId = notificationId,
                                errorResponse = null,
                            )
                        this@NotificationManager.deleteNotificationByIdState.emit(
                            deleteNotificationByIdState,
                        )
                    }

                    override suspend fun onError(errorObject: JSONObject) {
                        val deleteNotificationByIdState =
                            DeleteNotificationByIdState(
                                deleteStatus = null,
                                errorResponse = errorObject,
                            )
                        this@NotificationManager.deleteNotificationByIdState.emit(
                            deleteNotificationByIdState,
                        )
                    }
                },
        )
    }

    suspend fun clearAllNotifications(
        userToken: String,
        recipientId: String,
        startDate: String,
    ) {
        service.clearAllNotifications(
            userToken = userToken,
            recipientId = recipientId,
            startDate = startDate,
            networkCallback =
                object : NetworkCallback {
                    override suspend fun onResult(classObject: Any) {
                        val clearAllNotificationsState =
                            ClearAllNotificationsState(
                                clearAllNotificationsResponse = classObject as DataStatus,
                                errorResponse = null,
                            )
                        this@NotificationManager.clearAllNotificationsState.emit(
                            clearAllNotificationsState,
                        )
                    }

                    override suspend fun onError(errorObject: JSONObject) {
                        val clearAllNotificationsState =
                            ClearAllNotificationsState(
                                clearAllNotificationsResponse = null,
                                errorResponse = errorObject,
                            )
                        this@NotificationManager.clearAllNotificationsState.emit(
                            clearAllNotificationsState,
                        )
                    }
                },
        )
    }
}
