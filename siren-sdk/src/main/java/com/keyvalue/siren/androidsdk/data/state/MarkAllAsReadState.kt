package com.keyvalue.siren.androidsdk.data.state

import com.keyvalue.siren.androidsdk.data.model.DataStatus
import org.json.JSONObject

class MarkAllAsReadState(
    var errorResponse: JSONObject? = null,
    var markAllAsReadResponse: DataStatus? = null,
)
