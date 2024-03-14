package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Data(
    @SerializedName("status")
    val status: String?,
)
