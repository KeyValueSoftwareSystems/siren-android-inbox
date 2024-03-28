package com.keyvalue.siren.androidsdk.data.state

import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponseData
import org.json.JSONObject

class MarkAsViewedViewedState(
    var errorResponse: JSONObject? = null,
    var markAsViewedResponse: MarkAsViewedResponseData? = null,
)
