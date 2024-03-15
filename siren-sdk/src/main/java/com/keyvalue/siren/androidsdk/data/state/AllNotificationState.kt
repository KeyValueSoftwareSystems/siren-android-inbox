package com.keyvalue.siren.androidsdk.data.state

import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import org.json.JSONObject

class AllNotificationState(
    var errorResponse: JSONObject? = null,
    var allNotificationResponse: List<AllNotificationResponseData>? = null,
)
