package com.keyvalue.siren.androidsdk.data.model

import com.keyvalue.siren.androidsdk.utils.constants.BulkUpdateType

data class BulkUpdateBody(
    val until: String?,
    val operation: BulkUpdateType?,
)
