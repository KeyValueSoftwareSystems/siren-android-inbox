package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MarkAsReadByIdResponseData(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("deletedBy")
    val deletedBy: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("projectEnvironmentId")
    val projectEnvironmentId: String,
    @SerializedName("isRead")
    val isRead: Boolean,
    @SerializedName("isDelivered")
    val isDelivered: Boolean,
    @SerializedName("inAppRecipient")
    val inAppRecipient: InAppRecipient,
    @SerializedName("message")
    val message: Message,
    @SerializedName("requestId")
    val requestId: String,
)
