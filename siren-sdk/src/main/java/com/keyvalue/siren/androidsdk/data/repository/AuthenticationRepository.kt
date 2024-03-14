package com.keyvalue.siren.androidsdk.data.repository

import com.keyvalue.siren.androidsdk.data.networkcallbacks.NetworkCallback

interface AuthenticationRepository {
    suspend fun verifyToken(
        userToken: String,
        recipientId: String,
        networkCallback: NetworkCallback,
    )
}
