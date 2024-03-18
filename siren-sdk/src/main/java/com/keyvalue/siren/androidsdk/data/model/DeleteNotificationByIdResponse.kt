package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DeleteNotificationByIdResponse(
    @SerializedName("data")
    val deleteNotificationByIdResponse: DataStatus?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
