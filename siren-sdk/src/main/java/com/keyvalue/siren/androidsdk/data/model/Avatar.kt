package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Avatar(
    @SerializedName("actionUrl")
    val actionUrl: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
)
