package com.keyvalue.siren.androidsdk.data.model

import com.google.gson.annotations.SerializedName

data class MarkAllAsReadResponse(
    @SerializedName("data")
    val markAllAsReadResponse: DataStatus?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
