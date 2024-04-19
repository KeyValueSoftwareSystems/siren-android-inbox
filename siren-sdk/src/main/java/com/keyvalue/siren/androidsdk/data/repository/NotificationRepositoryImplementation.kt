package com.keyvalue.siren.androidsdk.data.repository

import com.google.gson.Gson
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
import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback
import com.keyvalue.siren.androidsdk.data.retrofit.RetrofitClient
import com.keyvalue.siren.androidsdk.data.service.NotificationApiService
import com.keyvalue.siren.androidsdk.utils.constants.API_ERROR
import com.keyvalue.siren.androidsdk.utils.constants.BULK_DELETE_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.BulkUpdateType
import com.keyvalue.siren.androidsdk.utils.constants.DELETE_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_API_ERROR
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_BULK_DELETE_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_DELETE_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_MARK_ALL_AS_READ_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_MARK_ALL_AS_VIEWED_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_MARK_AS_READ_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_NOTIFICATION_FETCH_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_TIMED_OUT
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_UNVIEWED_COUNT_FETCH_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.MARK_ALL_AS_READ_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.MARK_ALL_AS_VIEWED_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.MARK_AS_READ_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.NOTIFICATION_FETCH_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.SirenErrorTypes
import com.keyvalue.siren.androidsdk.utils.constants.TIMED_OUT
import com.keyvalue.siren.androidsdk.utils.constants.UNVIEWED_COUNT_FETCH_FAILED
import org.json.JSONObject
import java.net.SocketTimeoutException

class NotificationRepositoryImplementation(baseURL: String) : NotificationRepository {
    private var notificationsApiService: NotificationApiService? = null

    init {
        notificationsApiService =
            RetrofitClient.getRetrofitInstance(baseURL)?.create(
                NotificationApiService::class.java,
            )
    }

    override suspend fun fetchUnViewedNotificationsCount(
        userToken: String,
        recipientId: String,
        networkCallback: NetworkCallback,
    ) {
        try {
            val parentResponse =
                notificationsApiService?.fetchUnViewedNotificationsCount(recipientId, userToken)
            val response = parentResponse?.body()
            if (response?.unViewedNotificationResponse != null) {
                response.unViewedNotificationResponse.let {
                    networkCallback.onResult(it)
                }
            } else {
                val errorBody = parentResponse?.errorBody()?.string()
                if (errorBody != null) {
                    val errors =
                        Gson().fromJson<UnViewedNotificationResponse>(
                            errorBody,
                            UnViewedNotificationResponse::class.java,
                        )
                    networkCallback.onError(
                        JSONObject().put("type", SirenErrorTypes.ERROR)
                            .put("code", errors.error?.errorCode ?: UNVIEWED_COUNT_FETCH_FAILED).put(
                                "message",
                                errors.error?.message
                                    ?: ERROR_MESSAGE_UNVIEWED_COUNT_FETCH_FAILED,
                            ),
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", TIMED_OUT).put("message", ERROR_MESSAGE_TIMED_OUT),
            )
        } catch (e: Exception) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", API_ERROR)
                    .put("message", ERROR_MESSAGE_API_ERROR),
            )
        }
    }

    override suspend fun fetchAllNotifications(
        userToken: String,
        recipientId: String,
        page: Int?,
        size: Int?,
        start: String?,
        end: String?,
        isRead: Boolean?,
        networkCallback: NetworkCallback,
    ) {
        try {
            val parentResponse =
                notificationsApiService?.fetchAllNotifications(
                    recipientId,
                    userToken,
                    page,
                    size,
                    start,
                    end,
                    isRead,
                )
            val response = parentResponse?.body()
            if (parentResponse?.isSuccessful == true && response?.allNotificationResponse != null) {
                response.allNotificationResponse.let { networkCallback.onResult(it) }
            } else {
                val errorBody = parentResponse?.errorBody()?.string()
                if (errorBody != null) {
                    val errors =
                        Gson().fromJson<AllNotificationResponse>(
                            errorBody,
                            AllNotificationResponse::class.java,
                        )
                    networkCallback.onError(
                        JSONObject().put("type", SirenErrorTypes.ERROR)
                            .put("code", errors.error?.errorCode ?: NOTIFICATION_FETCH_FAILED).put(
                                "message",
                                errors.error?.message
                                    ?: ERROR_MESSAGE_NOTIFICATION_FETCH_FAILED,
                            ),
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", TIMED_OUT).put("message", ERROR_MESSAGE_TIMED_OUT),
            )
        } catch (e: Exception) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", API_ERROR)
                    .put("message", ERROR_MESSAGE_API_ERROR),
            )
        }
    }

    override suspend fun markAsReadById(
        userToken: String,
        recipientId: String,
        notificationId: String,
        networkCallback: NetworkCallback,
    ) {
        try {
            val parentResponse =
                notificationsApiService?.markAsReadById(
                    recipientId,
                    userToken,
                    notificationId,
                    MarkAsReadBody(isRead = true, isDelivered = false),
                )
            val response = parentResponse?.body()
            if (parentResponse?.isSuccessful == true && response?.markAsReadByIdResponse != null) {
                response.markAsReadByIdResponse.let { networkCallback.onResult(it) }
            } else {
                val errorBody = parentResponse?.errorBody()?.string()
                if (errorBody != null) {
                    val errors =
                        Gson().fromJson<MarkAsReadByIdResponse>(
                            errorBody,
                            MarkAsReadByIdResponse::class.java,
                        )
                    networkCallback.onError(
                        JSONObject().put("type", SirenErrorTypes.ERROR)
                            .put("code", errors.error?.errorCode ?: MARK_AS_READ_FAILED).put(
                                "message",
                                errors.error?.message
                                    ?: ERROR_MESSAGE_MARK_AS_READ_FAILED,
                            ),
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", TIMED_OUT).put("message", ERROR_MESSAGE_TIMED_OUT),
            )
        } catch (e: Exception) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", API_ERROR)
                    .put("message", ERROR_MESSAGE_API_ERROR),
            )
        }
    }

    override suspend fun markAllAsRead(
        userToken: String,
        recipientId: String,
        startDate: String,
        networkCallback: NetworkCallback,
    ) {
        try {
            val parentResponse =
                notificationsApiService?.markAllAsRead(
                    recipientId,
                    userToken,
                    BulkUpdateBody(
                        startDate,
                        operation = BulkUpdateType.MARK_AS_READ,
                    ),
                )
            val response = parentResponse?.body()
            if (parentResponse?.isSuccessful == true && response?.markAllAsReadResponse != null) {
                response.markAllAsReadResponse.let { networkCallback.onResult(it) }
            } else {
                val errorBody = parentResponse?.errorBody()?.string()
                if (errorBody != null) {
                    val errors =
                        Gson().fromJson<MarkAllAsReadResponse>(
                            errorBody,
                            MarkAllAsReadResponse::class.java,
                        )
                    networkCallback.onError(
                        JSONObject().put("type", SirenErrorTypes.ERROR)
                            .put("code", errors.error?.errorCode ?: MARK_ALL_AS_READ_FAILED).put(
                                "message",
                                errors.error?.message
                                    ?: ERROR_MESSAGE_MARK_ALL_AS_READ_FAILED,
                            ),
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", TIMED_OUT).put("message", ERROR_MESSAGE_TIMED_OUT),
            )
        } catch (e: Exception) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", API_ERROR)
                    .put("message", ERROR_MESSAGE_API_ERROR),
            )
        }
    }

    override suspend fun markAsViewed(
        userToken: String,
        recipientId: String,
        startDate: String,
        networkCallback: NetworkCallback,
    ) {
        try {
            val parentResponse =
                notificationsApiService?.markAsViewed(
                    recipientId,
                    userToken,
                    MarkAsViewedBody(
                        startDate,
                    ),
                )
            val response = parentResponse?.body()
            if (parentResponse?.isSuccessful == true && response?.markAsViewedResponse != null) {
                response.markAsViewedResponse.let { networkCallback.onResult(it) }
            } else {
                val errorBody = parentResponse?.errorBody()?.string()
                if (errorBody != null) {
                    val errors =
                        Gson().fromJson<MarkAsViewedResponse>(
                            errorBody,
                            MarkAsViewedResponse::class.java,
                        )
                    networkCallback.onError(
                        JSONObject().put("type", SirenErrorTypes.ERROR)
                            .put("code", errors.error?.errorCode ?: MARK_ALL_AS_VIEWED_FAILED).put(
                                "message",
                                errors.error?.message
                                    ?: ERROR_MESSAGE_MARK_ALL_AS_VIEWED_FAILED,
                            ),
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", TIMED_OUT).put("message", ERROR_MESSAGE_TIMED_OUT),
            )
        } catch (e: Exception) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", API_ERROR)
                    .put("message", ERROR_MESSAGE_API_ERROR),
            )
        }
    }

    override suspend fun deleteNotificationById(
        userToken: String,
        recipientId: String,
        notificationId: String,
        networkCallback: NetworkCallback,
    ) {
        try {
            val parentResponse =
                notificationsApiService?.deleteNotificationById(
                    recipientId,
                    userToken,
                    notificationId,
                )
            val response = parentResponse?.body()
            if (parentResponse?.isSuccessful == true && response?.deleteNotificationByIdResponse != null) {
                response.deleteNotificationByIdResponse.let { networkCallback.onResult(it) }
            } else {
                val errorBody = parentResponse?.errorBody()?.string()
                if (errorBody != null) {
                    val errors =
                        Gson().fromJson(errorBody, DeleteNotificationByIdResponse::class.java)
                    networkCallback.onError(
                        JSONObject().put("type", SirenErrorTypes.ERROR)
                            .put("code", errors.error?.errorCode ?: DELETE_FAILED).put(
                                "message",
                                errors.error?.message
                                    ?: ERROR_MESSAGE_DELETE_FAILED,
                            ),
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", TIMED_OUT).put("message", ERROR_MESSAGE_TIMED_OUT),
            )
        } catch (e: Exception) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", API_ERROR)
                    .put("message", ERROR_MESSAGE_API_ERROR),
            )
        }
    }

    override suspend fun clearAllNotifications(
        userToken: String,
        recipientId: String,
        startDate: String,
        networkCallback: NetworkCallback,
    ) {
        try {
            val parentResponse =
                notificationsApiService?.clearAllNotifications(
                    recipientId,
                    userToken,
                    BulkUpdateBody(
                        startDate,
                        operation = BulkUpdateType.MARK_AS_DELETED,
                    ),
                )
            val response = parentResponse?.body()
            if (parentResponse?.isSuccessful == true && response?.clearAllNotificationsResponse != null) {
                response.clearAllNotificationsResponse.let { networkCallback.onResult(it) }
            } else {
                val errorBody = parentResponse?.errorBody()?.string()
                if (errorBody != null) {
                    val errors =
                        Gson().fromJson<ClearAllNotificationsResponse>(
                            errorBody,
                            ClearAllNotificationsResponse::class.java,
                        )
                    networkCallback.onError(
                        JSONObject().put("type", SirenErrorTypes.ERROR)
                            .put("code", errors.error?.errorCode ?: BULK_DELETE_FAILED).put(
                                "message",
                                errors.error?.message
                                    ?: ERROR_MESSAGE_BULK_DELETE_FAILED,
                            ),
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", TIMED_OUT).put("message", ERROR_MESSAGE_TIMED_OUT),
            )
        } catch (e: Exception) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", API_ERROR)
                    .put("message", ERROR_MESSAGE_API_ERROR),
            )
        }
    }
}
