package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep

@Keep
data class MarkAsViewedResponseData(
    val createdAt: String,
    val createdBy: String,
    val deletedAt: String?,
    val deletedBy: String?,
    val id: String,
    val lastOpenedAt: String,
    val projectEnvironmentId: String,
    val providerIntegrationId: String,
    val referenceId: String,
    val updatedAt: String,
    val updatedBy: String,
)
