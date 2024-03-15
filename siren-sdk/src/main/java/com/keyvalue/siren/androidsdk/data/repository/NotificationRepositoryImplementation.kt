package com.keyvalue.siren.androidsdk.data.repository

import com.google.gson.Gson
import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponse
import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback
import com.keyvalue.siren.androidsdk.data.retrofit.RetrofitClient
import com.keyvalue.siren.androidsdk.data.service.NotificationApiService
import com.keyvalue.siren.androidsdk.utils.constants.CODE_GENERIC_API_ERROR
import com.keyvalue.siren.androidsdk.utils.constants.CODE_TIMED_OUT
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_SERVICE_NOT_AVAILABLE
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_TIMED_OUT
import com.keyvalue.siren.androidsdk.utils.constants.SirenErrorTypes
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
                            .put("code", errors.error?.errorCode ?: CODE_GENERIC_API_ERROR).put(
                                "message",
                                errors.error?.message
                                    ?: "HTTP error! status: ${parentResponse.raw().code} ${parentResponse.raw().message}",
                            ),
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            networkCallback.onError(
                JSONObject().put("code", CODE_TIMED_OUT).put("message", ERROR_MESSAGE_TIMED_OUT),
            )
        } catch (e: Exception) {
            networkCallback.onError(
                JSONObject().put("code", CODE_GENERIC_API_ERROR)
                    .put("message", ERROR_MESSAGE_SERVICE_NOT_AVAILABLE),
            )
        }
    }
}
