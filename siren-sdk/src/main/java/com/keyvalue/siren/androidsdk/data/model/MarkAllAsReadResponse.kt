package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MarkAllAsReadResponse(
    @SerializedName("data")
    val markAllAsReadResponse: DataStatus?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
