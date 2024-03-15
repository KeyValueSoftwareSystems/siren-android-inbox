package com.keyvalue.siren.androidsdk.data.repository

import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback

interface NotificationRepository {
    suspend fun fetchUnViewedNotificationsCount(
        userToken: String,
        recipientId: String,
        networkCallback: NetworkCallback,
    )
}
