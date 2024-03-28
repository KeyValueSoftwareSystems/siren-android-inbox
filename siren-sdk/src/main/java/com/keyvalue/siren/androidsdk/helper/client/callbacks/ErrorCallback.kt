package com.keyvalue.siren.androidsdk.helper.client.callbacks

import org.json.JSONObject

interface ErrorCallback {
    fun onError(jsonObject: JSONObject)
}
