package com.keyvalue.siren.androidsdk.data.model

import com.google.gson.annotations.SerializedName

data class MarkAsReadByIdResponse(
    @SerializedName("data")
    val markAsReadByIdResponse: MarkAsReadByIdResponseData,
    @SerializedName("error")
    val error: ErrorResponse?,
)
