package com.keyvalue.siren.androidsdk.data.model

import androidx.annotation.Keep
import com.keyvalue.siren.androidsdk.utils.constants.BulkUpdateType

@Keep
data class BulkUpdateBody(
    val until: String?,
    val operation: BulkUpdateType?,
)
