package com.keyvalue.siren.androidsdk.data.state

import com.keyvalue.siren.androidsdk.data.model.UnViewedNotificationResponseData
import org.json.JSONObject

class NotificationUnViewedState(
    var errorResponse: JSONObject? = null,
    var notificationUnViewedResponse: UnViewedNotificationResponseData? = null,
)
