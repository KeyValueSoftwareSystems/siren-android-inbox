package com.keyvalue.siren.androidsdk.data.managers

import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback
import com.keyvalue.siren.androidsdk.data.repository.NotificationRepository
import com.keyvalue.siren.androidsdk.data.repository.NotificationRepositoryImplementation
import com.keyvalue.siren.androidsdk.data.state.NotificationUnViewedState
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONObject

class NotificationManager(baseURL: String) {
    private var service: NotificationRepository =
        NotificationRepositoryImplementation(baseURL)
    var notificationUnViewedState: MutableStateFlow<NotificationUnViewedState?> =
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
}
