package com.keyvalue.siren.androidsdk.data.managers

import com.keyvalue.siren.androidsdk.data.model.Data
import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback
import com.keyvalue.siren.androidsdk.data.repository.AuthenticationRepository
import com.keyvalue.siren.androidsdk.data.repository.AuthenticationRepositoryImplementation
import com.keyvalue.siren.androidsdk.data.state.AuthenticationState
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONObject

class AuthenticationManager(baseURL: String) {
    private var service: AuthenticationRepository = AuthenticationRepositoryImplementation(baseURL)
    var authenticationState: MutableStateFlow<AuthenticationState?> = MutableStateFlow(null)

    suspend fun verifyToken(
        userToken: String,
        recipientId: String,
    ) {
        service.verifyToken(
            userToken,
            recipientId,
            object : NetworkCallback {
                override suspend fun onResult(classObject: Any) {
                    val authenticationData = classObject as Data

                    val authenticationState =
                        AuthenticationState(
                            isAuthenticated = authenticationData.status == "SUCCESS",
                            errorResponse = null,
                        )
                    this@AuthenticationManager.authenticationState.emit(authenticationState)
                }

                override suspend fun onError(errorObject: JSONObject) {
                    val authenticationState =
                        AuthenticationState(isAuthenticated = false, errorResponse = errorObject)
                    this@AuthenticationManager.authenticationState.emit(authenticationState)
                }
            },
        )
    }
}
