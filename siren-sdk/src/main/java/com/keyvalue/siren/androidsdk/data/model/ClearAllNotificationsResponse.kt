package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ClearAllNotificationsResponse(
    @SerializedName("data")
    val clearAllNotificationsResponse: DataStatus,
    @SerializedName("error")
    val error: ErrorResponse?,
)
