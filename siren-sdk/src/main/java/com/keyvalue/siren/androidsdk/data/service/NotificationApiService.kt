package com.keyvalue.siren.androidsdk.data.service

import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface NotificationApiService {
    @GET("api/v2/in-app/recipients/{inAppRecipientId}")
    suspend fun fetchUnViewedNotificationsCount(
        @Path("inAppRecipientId") id: String,
        @Header("Authorization") token: String,
    ): Response<UnViewedNotificationResponse>
}
