package com.keyvalue.siren.androidsdk.data.state

import com.keyvalue.siren.androidsdk.data.model.DataStatus
import org.json.JSONObject

data class DeleteNotificationByIdState(
    var errorResponse: JSONObject? = null,
    var deleteStatus: DataStatus? = null,
    var deleteId: String? = null,
)
