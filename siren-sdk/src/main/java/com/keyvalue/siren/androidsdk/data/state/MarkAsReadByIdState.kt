package com.keyvalue.siren.androidsdk.data.state

import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import org.json.JSONObject

class MarkAsReadByIdState(
    var errorResponse: JSONObject? = null,
    var markAsReadByIdResponse: MarkAsReadByIdResponseData? = null,
)
