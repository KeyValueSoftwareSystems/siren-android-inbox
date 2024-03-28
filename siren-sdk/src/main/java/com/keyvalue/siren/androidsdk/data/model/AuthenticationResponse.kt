package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AuthenticationResponse(
    @SerializedName("data")
    val responseData: Data?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
