package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MarkAsViewedResponse(
    @SerializedName("data")
    val markAsViewedResponse: MarkAsViewedResponseData?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
