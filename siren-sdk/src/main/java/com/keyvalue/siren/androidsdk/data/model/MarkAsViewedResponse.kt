package com.keyvalue.siren.androidsdk.data.model

import com.google.gson.annotations.SerializedName

data class MarkAsViewedResponse(
    @SerializedName("data")
    val markAsViewedResponse: MarkAsViewedResponseData?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
