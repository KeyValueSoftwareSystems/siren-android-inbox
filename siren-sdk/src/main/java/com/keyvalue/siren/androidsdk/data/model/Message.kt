package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Message(
    @SerializedName("actionUrl")
    val actionUrl: String?,
    @SerializedName("additionalData")
    val additionalData: String?,
    @SerializedName("avatar")
    val avatar: Avatar,
    @SerializedName("body")
    val body: String,
    @SerializedName("channel")
    val channel: String,
    @SerializedName("header")
    val header: String?,
    @SerializedName("subHeader")
    val subHeader: String?,
)
