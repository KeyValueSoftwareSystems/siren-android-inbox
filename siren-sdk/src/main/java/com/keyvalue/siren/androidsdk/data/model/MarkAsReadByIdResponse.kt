package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MarkAsReadByIdResponse(
    @SerializedName("data")
    val markAsReadByIdResponse: MarkAsReadByIdResponseData,
    @SerializedName("error")
    val error: ErrorResponse?,
)
