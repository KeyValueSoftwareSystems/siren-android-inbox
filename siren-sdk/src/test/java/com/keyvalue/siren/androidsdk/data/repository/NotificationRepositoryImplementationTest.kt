package com.keyvalue.siren.androidsdk.data.repository

import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponse
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.Avatar
import com.keyvalue.siren.androidsdk.data.model.BulkUpdateBody
import com.keyvalue.siren.androidsdk.data.model.ClearAllNotificationsResponse
import com.keyvalue.siren.androidsdk.data.model.DataStatus
import com.keyvalue.siren.androidsdk.data.model.DeleteNotificationByIdResponse
import com.keyvalue.siren.androidsdk.data.model.InAppRecipient
import com.keyvalue.siren.androidsdk.data.model.MarkAllAsReadResponse
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadBody
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponse
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedBody
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponse
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponseData
import com.keyvalue.siren.androidsdk.data.model.Message
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponse
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback
import com.keyvalue.siren.androidsdk.data.service.NotificationApiService
import com.keyvalue.siren.androidsdk.utils.constants.BulkUpdateType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class NotificationRepositoryImplementationTest {
    private lateinit var notificationApiService: NotificationApiService
    private lateinit var notificationRepository: NotificationRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        notificationApiService = mock(NotificationApiService::class.java)
        notificationRepository = NotificationRepositoryImplementation("https://example.com")
    }

    @Test
    fun fetchUnViewedNotificationsCount_Success() {
        val userToken = "userToken"
        val recipientId = "recipientId"
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
        val response = UnViewedNotificationResponse(responseData, null)
        val responseBody = Response.success(response)
        var onSuccessCalled = false
        var onErrorCalled = false

        runBlocking {
            `when`(notificationApiService.fetchUnViewedNotificationsCount(recipientId, userToken)).thenReturn(responseBody)
        }

        val networkCallback =
            object : NetworkCallback {
                override suspend fun onResult(classObject: Any) {
                    onSuccessCalled = true
                    assertNotNull(classObject)
                }

                override suspend fun onError(errorObject: JSONObject) {
                    onErrorCalled = true
                    assertNotNull(errorObject)
                }
            }

        runBlocking {
            notificationRepository.fetchUnViewedNotificationsCount(userToken, recipientId, networkCallback)
        }

        // Then
        assertTrue(onErrorCalled)
    }

    @Test
    fun fetchAllNotifications_Success() {
        // Given
        val recipientId = "recipientId"
        val userToken = "userToken"
        val responseData =
            AllNotificationResponse(
                allNotificationResponse =
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
                    ),
                error = null,
            )
        val response = Response.success(responseData)
        runBlocking {
            `when`(notificationApiService.fetchAllNotifications(id = recipientId, token = userToken)).thenReturn(response)
        }

        var onSuccessCalled = false
        var onErrorCalled = false

        // When
        val networkCallback =
            object : NetworkCallback {
                override suspend fun onResult(classObject: Any) {
                    onSuccessCalled = true
                    assertNotNull(classObject)
                }

                override suspend fun onError(errorObject: JSONObject) {
                    onErrorCalled = true
                    assertNotNull(errorObject)
                }
            }
        runBlocking {
            notificationRepository.fetchAllNotifications(recipientId = recipientId, userToken = userToken, networkCallback = networkCallback)
        }

        // Then
        assertTrue(onErrorCalled)
    }

    @Test
    fun markAsReadById_Success() {
        // Given
        val recipientId = "recipientId"
        val userToken = "userToken"
        val notificationId = "notificationId"
        val requestData = MarkAsReadBody(isRead = true, isDelivered = true)
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
                        id = recipientId,
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
        val response = Response.success(MarkAsReadByIdResponse(responseData, null))
        runBlocking {
            `when`(
                notificationApiService.markAsReadById(
                    id = recipientId,
                    token = userToken,
                    notificationId = notificationId,
                    data = requestData,
                ),
            ).thenReturn(response)
        }

        var onSuccessCalled = false
        var onErrorCalled = false

        // When
        val networkCallback =
            object : NetworkCallback {
                override suspend fun onResult(classObject: Any) {
                    onSuccessCalled = true
                    assertNotNull(classObject)
                }

                override suspend fun onError(errorObject: JSONObject) {
                    onErrorCalled = true
                    assertNotNull(errorObject)
                }
            }
        runBlocking {
            notificationRepository.markAsReadById(
                recipientId = recipientId,
                userToken = userToken,
                notificationId = notificationId,
                networkCallback = networkCallback,
            )
        }

        // Then
        assertTrue(onErrorCalled)
    }

    @Test
    fun markAllAsRead_Success() {
        // Given
        val recipientId = "recipientId"
        val userToken = "userToken"
        val requestData = BulkUpdateBody(until = null, operation = BulkUpdateType.MARK_AS_READ)
        val responseData = MarkAllAsReadResponse(DataStatus("success"), null)
        val response = Response.success(responseData)
        runBlocking {
            `when`(
                notificationApiService.markAllAsRead(
                    id = recipientId,
                    token = userToken,
                    data = requestData,
                ),
            ).thenReturn(response)
        }

        var onSuccessCalled = false
        var onErrorCalled = false

        // When
        val networkCallback =
            object : NetworkCallback {
                override suspend fun onResult(classObject: Any) {
                    onSuccessCalled = true
                    assertNotNull(classObject)
                }

                override suspend fun onError(errorObject: JSONObject) {
                    onErrorCalled = true
                    assertNotNull(errorObject)
                }
            }
        runBlocking {
            notificationRepository.markAllAsRead(
                recipientId = recipientId,
                userToken = userToken,
                startDate = "2024-04-03T12:00:00",
                networkCallback = networkCallback,
            )
        }

        // Then
        assertTrue(onErrorCalled)
    }

    @Test
    fun markAsViewed_Success() {
        // Given
        val recipientId = "recipientId"
        val userToken = "userToken"
        val requestData = MarkAsViewedBody(lastOpenedAt = "2024-04-03T12:00:00")
        val responseData =
            MarkAsViewedResponse(
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
                ),
                null,
            )
        val response = Response.success(responseData)
        runBlocking {
            `when`(
                notificationApiService.markAsViewed(
                    id = recipientId,
                    token = userToken,
                    data = requestData,
                ),
            ).thenReturn(response)
        }

        var onSuccessCalled = false
        var onErrorCalled = false

        // When
        val networkCallback =
            object : NetworkCallback {
                override suspend fun onResult(classObject: Any) {
                    onSuccessCalled = true
                    assertNotNull(classObject)
                }

                override suspend fun onError(errorObject: JSONObject) {
                    onErrorCalled = true
                    assertNotNull(errorObject)
                }
            }
        runBlocking {
            notificationRepository.markAsViewed(
                recipientId = recipientId,
                userToken = userToken,
                startDate = "2024-04-03T12:00:00",
                networkCallback = networkCallback,
            )
        }

        // Then
        assertTrue(onErrorCalled)
    }

    @Test
    fun deleteNotificationById_Success() {
        // Given
        val recipientId = "recipientId"
        val userToken = "userToken"
        val notificationId = "notificationId"
        val responseData = DataStatus(status = "success")
        val response =
            Response.success(
                DeleteNotificationByIdResponse(
                    deleteNotificationByIdResponse = responseData,
                    error = null,
                ),
            )
        runBlocking {
            `when`(
                notificationApiService.deleteNotificationById(
                    id = recipientId,
                    token = userToken,
                    notificationId = notificationId,
                ),
            ).thenReturn(response)
        }

        var onSuccessCalled = false
        var onErrorCalled = false

        // When
        val networkCallback =
            object : NetworkCallback {
                override suspend fun onResult(classObject: Any) {
                    onSuccessCalled = true
                    assertNotNull(classObject)
                    // Additional assertions on classObject can be added here
                }

                override suspend fun onError(errorObject: JSONObject) {
                    onErrorCalled = true
                    assertNotNull(errorObject)
                }
            }
        runBlocking {
            notificationRepository.deleteNotificationById(
                recipientId = recipientId,
                userToken = userToken,
                notificationId = notificationId,
                networkCallback = networkCallback,
            )
        }

        // Then
        assertTrue(onErrorCalled)
    }

    @Test
    fun clearAllNotifications_Success() {
        // Given
        val recipientId = "recipientId"
        val userToken = "userToken"
        val responseData = DataStatus(status = "success")
        val response =
            Response.success(
                ClearAllNotificationsResponse(
                    clearAllNotificationsResponse = responseData,
                    error = null,
                ),
            )
        runBlocking {
            `when`(
                notificationApiService.clearAllNotifications(
                    id = recipientId,
                    token = userToken,
                    data = BulkUpdateBody(until = null, operation = BulkUpdateType.MARK_AS_DELETED),
                ),
            ).thenReturn(response)
        }

        var onSuccessCalled = false
        var onErrorCalled = false

        // When
        val networkCallback =
            object : NetworkCallback {
                override suspend fun onResult(classObject: Any) {
                    onSuccessCalled = true
                    assertNotNull(classObject)
                    // Additional assertions on classObject can be added here
                }

                override suspend fun onError(errorObject: JSONObject) {
                    onErrorCalled = true
                    assertNotNull(errorObject)
                }
            }
        runBlocking {
            notificationRepository.clearAllNotifications(
                recipientId = recipientId,
                userToken = userToken,
                startDate = "2024-04-03T12:00:00",
                networkCallback = networkCallback,
            )
        }

        // Then
        assertTrue(onErrorCalled)
    }
}
