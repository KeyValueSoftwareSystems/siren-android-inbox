package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorResponse(
    @SerializedName("errorCode")
    val errorCode: String?,
    @SerializedName("message")
    val message: String?,
)
