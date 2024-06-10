package com.keyvalue.siren.androidsdk.data.repository

import com.google.gson.Gson
import com.keyvalue.siren.androidsdk.data.model.AuthenticationResponse
import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback
import com.keyvalue.siren.androidsdk.data.retrofit.RetrofitClient
import com.keyvalue.siren.androidsdk.data.service.AuthenticationApiService
import com.keyvalue.siren.androidsdk.utils.constants.API_ERROR
import com.keyvalue.siren.androidsdk.utils.constants.AUTHENTICATION_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_API_ERROR
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_AUTHENTICATION_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_TIMED_OUT
import com.keyvalue.siren.androidsdk.utils.constants.SirenErrorTypes
import com.keyvalue.siren.androidsdk.utils.constants.TIMED_OUT
import org.json.JSONObject
import java.net.SocketTimeoutException

class AuthenticationRepositoryImplementation(baseURL: String) : AuthenticationRepository {
    private var authenticationApiService: AuthenticationApiService? = null

    init {
        authenticationApiService =
            RetrofitClient.getRetrofitInstance(baseURL)?.create(
                AuthenticationApiService::class.java,
            )
    }

    override suspend fun verifyToken(
        userToken: String,
        recipientId: String,
        networkCallback: NetworkCallback,
    ) {
        try {
            val parentResponse = authenticationApiService?.verifyToken(recipientId, userToken)
            val response = parentResponse?.body()
            if (response?.responseData != null) {
                response.responseData.let { networkCallback.onResult(it) }
            } else {
                val errorBody = parentResponse?.errorBody()?.string()
                if (errorBody != null) {
                    val errors = Gson().fromJson<AuthenticationResponse>(errorBody, AuthenticationResponse::class.java)
                    networkCallback.onError(
                        JSONObject()
                            .put("type", SirenErrorTypes.ERROR)
                            .put("code", errors.error?.errorCode ?: AUTHENTICATION_FAILED)
                            .put("message", errors.error?.message ?: ERROR_MESSAGE_AUTHENTICATION_FAILED),
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            networkCallback.onError(
                JSONObject().put("type", SirenErrorTypes.ERROR).put("code", TIMED_OUT).put("message", ERROR_MESSAGE_TIMED_OUT),
            )
        } catch (e: Exception) {
            networkCallback.onError(JSONObject().put("type", SirenErrorTypes.ERROR).put("code", API_ERROR).put("message", ERROR_MESSAGE_API_ERROR))
        }
    }
}
