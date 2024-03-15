package com.keyvalue.siren.androidsdk.data.service

import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponse
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadBody
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponse
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApiService {
    @GET("api/v2/in-app/recipients/{inAppRecipientId}")
    suspend fun fetchUnViewedNotificationsCount(
        @Path("inAppRecipientId") id: String,
        @Header("Authorization") token: String,
    ): Response<UnViewedNotificationResponse>

    @GET("api/v2/in-app/recipients/{inAppRecipientId}/notifications")
    suspend fun fetchAllNotifications(
        @Path("inAppRecipientId") id: String,
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("start") start: String? = null,
        @Query("end") end: String? = null,
        @Query("isRead") isRead: Boolean? = null,
        @Query("sort") sort: String? = "createdAt",
    ): Response<AllNotificationResponse>

    @PATCH("api/v2/in-app/recipients/{inAppRecipientId}/notifications/{inAppNotificationId}")
    suspend fun markAsReadById(
        @Path("inAppRecipientId") id: String,
        @Header("Authorization") token: String,
        @Path("inAppNotificationId") notificationId: String,
        @Body data: MarkAsReadBody,
    ): Response<MarkAsReadByIdResponse>
}
