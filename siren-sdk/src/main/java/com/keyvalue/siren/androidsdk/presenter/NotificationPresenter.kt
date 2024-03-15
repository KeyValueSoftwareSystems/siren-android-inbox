package com.keyvalue.siren.androidsdk.presenter

import com.keyvalue.siren.androidsdk.data.managers.NotificationManager
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
}
