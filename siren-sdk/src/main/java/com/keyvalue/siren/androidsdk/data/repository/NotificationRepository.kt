package com.keyvalue.siren.androidsdk.data.repository

import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback

interface NotificationRepository {
    suspend fun fetchUnViewedNotificationsCount(
        userToken: String,
        recipientId: String,
        networkCallback: NetworkCallback,
    )

    suspend fun fetchAllNotifications(
        userToken: String,
        recipientId: String,
        page: Int? = null,
        size: Int? = null,
        start: String? = null,
        end: String? = null,
        isRead: Boolean? = null,
        networkCallback: NetworkCallback,
    )
}
