package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class InAppRecipient(
    @SerializedName("id")
    val id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("createdBy")
    val createdBy: String,
    @SerializedName("updatedBy")
    val updatedBy: String,
    @SerializedName("deletedBy")
    val deletedBy: String?,
    @SerializedName("projectEnvironmentId")
    val projectEnvironmentId: String,
    @SerializedName("referenceId")
    val referenceId: String,
    @SerializedName("providerIntegrationId")
    val providerIntegrationId: String,
    @SerializedName("lastOpenedAt")
    val lastOpenedAt: String,
)
