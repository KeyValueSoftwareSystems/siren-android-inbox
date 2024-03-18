package com.keyvalue.siren.androidsdk.data.state

import com.keyvalue.siren.androidsdk.data.model.DataStatus
import org.json.JSONObject

class ClearAllNotificationsState(
    var errorResponse: JSONObject? = null,
    var clearAllNotificationsResponse: DataStatus? = null,
)
