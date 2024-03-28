package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AllNotificationResponse(
    @SerializedName("data")
    val allNotificationResponse: List<AllNotificationResponseData?>?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
