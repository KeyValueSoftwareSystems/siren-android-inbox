package com.keyvalue.siren.androidsdk.data.service

import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponse
import com.keyvalue.siren.androidsdk.data.model.BulkUpdateBody
import com.keyvalue.siren.androidsdk.data.model.ClearAllNotificationsResponse
import com.keyvalue.siren.androidsdk.data.model.DeleteNotificationByIdResponse
import com.keyvalue.siren.androidsdk.data.model.MarkAllAsReadResponse
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadBody
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponse
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedBody
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponse
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
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

    @POST("api/v2/in-app/recipients/{inAppRecipientId}/notifications/bulk-update")
    suspend fun markAllAsRead(
        @Path("inAppRecipientId") id: String,
        @Header("Authorization") token: String,
        @Body data: BulkUpdateBody,
    ): Response<MarkAllAsReadResponse>

    @PATCH("api/v2/in-app/recipients/{inAppRecipientId}")
    suspend fun markAsViewed(
        @Path("inAppRecipientId") id: String,
        @Header("Authorization") token: String,
        @Body data: MarkAsViewedBody,
    ): Response<MarkAsViewedResponse>

    @DELETE("api/v2/in-app/recipients/{inAppRecipientId}/notifications/{inAppNotificationId}")
    suspend fun deleteNotificationById(
        @Path("inAppRecipientId") id: String,
        @Header("Authorization") token: String,
        @Path("inAppNotificationId") notificationId: String,
    ): Response<DeleteNotificationByIdResponse>

    @POST("api/v2/in-app/recipients/{inAppRecipientId}/notifications/bulk-update")
    suspend fun clearAllNotifications(
        @Path("inAppRecipientId") id: String,
        @Header("Authorization") token: String,
        @Body data: BulkUpdateBody,
    ): Response<ClearAllNotificationsResponse>
}
