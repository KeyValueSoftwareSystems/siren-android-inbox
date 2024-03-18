package com.keyvalue.siren.androidsdk.helper.client.callbacks

import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import org.json.JSONObject

interface MarkAsReadByIdCallback {
    fun onSuccess(responseData: MarkAsReadByIdResponseData?)

    fun onError(jsonObject: JSONObject)
}
