package com.keyvalue.siren.androidsdk.presenter

import android.content.Context
import com.keyvalue.siren.androidsdk.data.managers.NotificationManager
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.Avatar
import com.keyvalue.siren.androidsdk.data.model.DataStatus
import com.keyvalue.siren.androidsdk.data.model.InAppRecipient
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponseData
import com.keyvalue.siren.androidsdk.data.model.Message
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NotificationPresenterTest {
    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockNotificationManager: NotificationManager

    private lateinit var notificationPresenter: NotificationPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        notificationPresenter = NotificationPresenter(mockContext, "userToken", "recipientId")
    }

    @Test
    fun fetchUnViewedNotificationsCount_Success() {
        val responseData =
            UnViewedNotificationResponseData(
                id = "notificationId",
                createdAt = "2024-04-03T12:00:00Z",
                updatedAt = "2024-04-03T12:00:00Z",
                deletedAt = null,
                createdBy = "user123",
                updatedBy = "user123",
                deletedBy = null,
                projectEnvironmentId = "projectId",
                referenceId = "referenceId",
                providerIntegrationId = "providerId",
                lastOpenedAt = null,
                totalUnviewed = 5,
            )

        runBlocking {
            `when`(mockNotificationManager.fetchUnViewedNotificationsCount("userToken", "recipientId")).then {
                val callback: (UnViewedNotificationResponseData?, JSONObject?) -> Unit = it.getArgument(0)
                callback(responseData, null)
            }
        }

        notificationPresenter.fetchUnViewedNotificationsCount { response, _, _ ->
            assertEquals(responseData, response)
        }
    }

    @Test
    fun fetchAllNotifications_Success() {
        val responseData =
            listOf(
                AllNotificationResponseData(
                    id = "notificationId1",
                    createdAt = "2024-04-03T12:00:00",
                    isRead = false,
                    message =
                        Message(
                            actionUrl = "https://example.com",
                            additionalData = null,
                            avatar = Avatar("https://example.com/avatar.jpg", "https://example.com/avatar_image.jpg"),
                            body = "Notification body",
                            channel = "email",
                            header = "Notification header",
                            subHeader = "Notification subheader",
                        ),
                    requestId = "requestId1",
                ),
            )

        runBlocking {
            `when`(mockNotificationManager.fetchAllNotifications("userToken", "recipientId")).then {
                val callback: (List<AllNotificationResponseData>?, JSONObject?) -> Unit = it.getArgument(0)
                callback(responseData, null)
            }
        }

        notificationPresenter.fetchAllNotifications { response, _, _ ->
            assertEquals(responseData, response)
        }
    }

    @Test
    fun markAsReadById_Success() {
        val notificationId = "notificationId"
        val responseData =
            MarkAsReadByIdResponseData(
                createdAt = "2024-04-03T12:00:00",
                createdBy = "userId",
                updatedAt = "2024-04-03T12:00:00",
                updatedBy = "userId",
                deletedAt = null,
                deletedBy = null,
                id = notificationId,
                projectEnvironmentId = "projectId",
                isRead = true,
                isDelivered = true,
                inAppRecipient =
                    InAppRecipient(
                        id = "recipientId",
                        createdAt = "2024-04-03T12:00:00",
                        updatedAt = "2024-04-03T12:00:00",
                        deletedAt = null,
                        createdBy = "userId",
                        updatedBy = "userId",
                        deletedBy = null,
                        projectEnvironmentId = "projectId",
                        referenceId = "referenceId",
                        providerIntegrationId = "providerIntegrationId",
                        lastOpenedAt = "2024-04-03T12:00:00",
                    ),
                message =
                    Message(
                        actionUrl = "https://example.com",
                        additionalData = null,
                        avatar =
                            Avatar(
                                "https://example.com/avatar_small.jpg",
                                "https://example.com/avatar_image_large.jpg",
                            ),
                        body = "Notification body",
                        channel = "email",
                        header = "Notification header",
                        subHeader = "Notification subheader",
                    ),
                requestId = "requestId",
            )

        runBlocking {
            `when`(
                mockNotificationManager.markAsReadById(
                    "userToken",
                    "recipientId",
                    notificationId,
                ),
            ).then {
                val callback: (MarkAsReadByIdResponseData?, JSONObject?) -> Unit = it.getArgument(0)
                callback(responseData, null)
            }
        }

        notificationPresenter.markAsReadById(notificationId) { response, _, _ ->
            assertEquals(responseData, response)
        }
    }

    @Test
    fun markAllAsRead_Success() {
        val responseData = DataStatus("success")

        runBlocking {
            `when`(
                mockNotificationManager.markAllAsRead(
                    "userToken",
                    "recipientId",
                    "2024-04-03T12:00:00",
                ),
            ).then {
                val callback: (DataStatus?, JSONObject?) -> Unit = it.getArgument(0)
                callback(responseData, null)
            }
        }

        notificationPresenter.markAllAsRead("2024-04-03T12:00:00") { response, _, _ ->
            assertEquals(responseData, response)
        }
    }

    @Test
    fun markAsViewed_Success() {
        val responseData =
            MarkAsViewedResponseData(
                createdAt = "2024-04-03T12:00:00",
                createdBy = "userId",
                deletedAt = null,
                deletedBy = null,
                id = "notificationId",
                lastOpenedAt = "2024-04-03T12:00:00",
                projectEnvironmentId = "projectId",
                providerIntegrationId = "integrationId",
                referenceId = "referenceId",
                updatedAt = "2024-04-03T12:00:00",
                updatedBy = "userId",
            )

        runBlocking {
            `when`(
                mockNotificationManager.markAsViewed(
                    "userToken",
                    "recipientId",
                    "2024-04-03T12:00:00",
                ),
            ).then {
                val callback: (MarkAsViewedResponseData?, JSONObject?) -> Unit = it.getArgument(0)
                callback(responseData, null)
            }
        }

        notificationPresenter.markAsViewed("2024-04-03T12:00:00") { response, _, _ ->
            assertEquals(responseData, response)
        }
    }

    @Test
    fun deleteNotificationById_Success() {
        val responseData = DataStatus(status = "success")
        val notificationId = "notificationId"

        runBlocking {
            `when`(
                mockNotificationManager.deleteNotificationById(
                    "userToken",
                    "recipientId",
                    notificationId,
                ),
            ).then {
                val callback: (DataStatus?, JSONObject?) -> Unit = it.getArgument(0)
                callback(responseData, null)
            }
        }

        notificationPresenter.deleteNotificationById(notificationId) { response, _, _, _ ->
            assertEquals(responseData, response)
        }
    }

    @Test
    fun testClearAllNotifications_Success() {
        val responseData = DataStatus(status = "success")

        runBlocking {
            `when`(
                mockNotificationManager.clearAllNotifications(
                    "userToken",
                    "recipientId",
                    "2024-04-03T12:00:00",
                ),
            ).then {
                val callback: (DataStatus?, JSONObject?) -> Unit = it.getArgument(0)
                callback(responseData, null)
            }
        }

        notificationPresenter.clearAllNotifications("2024-04-03T12:00:00") { response, _, _ ->
            assertEquals(responseData, response)
        }
    }
}
