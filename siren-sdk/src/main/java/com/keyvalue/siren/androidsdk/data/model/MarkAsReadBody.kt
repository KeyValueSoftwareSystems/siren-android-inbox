package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep

@Keep
data class MarkAsReadBody(
    val isRead: Boolean?,
    val isDelivered: Boolean?,
)
