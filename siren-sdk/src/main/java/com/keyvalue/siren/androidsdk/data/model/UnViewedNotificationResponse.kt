package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UnViewedNotificationResponse(
    @SerializedName("data")
    val unViewedNotificationResponse: UnViewedNotificationResponseData?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
