package com.keyvalue.siren.androidsdk.helper.client.callbacks

import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import org.json.JSONObject

interface MarkAllAsReadCallback {
    fun onSuccess(responseData: AllNotificationResponseData)

    fun onError(jsonObject: JSONObject)
}
