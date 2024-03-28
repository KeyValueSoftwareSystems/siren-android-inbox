package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AllNotificationResponseData(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isRead")
    val isRead: Boolean,
    @SerializedName("message")
    val message: Message,
    @SerializedName("requestId")
    val requestId: String,
)
