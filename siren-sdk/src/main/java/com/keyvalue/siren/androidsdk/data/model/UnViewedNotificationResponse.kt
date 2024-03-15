package com.keyvalue.siren.androidsdk.data.model

import com.google.gson.annotations.SerializedName

data class UnViewedNotificationResponse(
    @SerializedName("data")
    val unViewedNotificationResponse: UnViewedNotificationResponseData?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
