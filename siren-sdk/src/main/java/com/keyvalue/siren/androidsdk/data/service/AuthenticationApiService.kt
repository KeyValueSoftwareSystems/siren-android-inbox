package com.keyvalue.siren.androidsdk.data.service

import com.keyvalue.siren.androidsdk.data.model.AuthenticationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AuthenticationApiService {
    @GET("api/v2/in-app/recipients/{inAppRecipientId}/verify-token")
    suspend fun verifyToken(
        @Path("inAppRecipientId") id: String,
        @Header("Authorization") token: String,
    ): Response<AuthenticationResponse>
}
