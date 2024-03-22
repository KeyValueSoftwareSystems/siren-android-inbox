package com.keyvalue.siren.androidsdk.helper.client.callbacks

import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponseData
import org.json.JSONObject

/**
 * Callback interface for handling the result of marking notifications as viewed.
 */
interface MarkAsViewedCallback {
    /**
     * Called when marking notifications as viewed is successful.
     *
     * @param responseData The response data containing information about the operation.
     */
    fun onSuccess(responseData: MarkAsViewedResponseData)

    /**
     * Called when an error occurs while marking notifications as viewed.
     *
     * @param jsonObject The JSON object representing the error.
     */
    fun onError(jsonObject: JSONObject)
}
