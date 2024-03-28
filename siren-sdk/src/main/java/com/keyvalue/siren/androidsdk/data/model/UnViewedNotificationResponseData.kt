package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep

@Keep
data class UnViewedNotificationResponseData(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val createdBy: String,
    val updatedBy: String,
    val deletedBy: String?,
    val projectEnvironmentId: String,
    val referenceId: String,
    val providerIntegrationId: String,
    val lastOpenedAt: String?,
    val totalUnviewed: Long,
)
