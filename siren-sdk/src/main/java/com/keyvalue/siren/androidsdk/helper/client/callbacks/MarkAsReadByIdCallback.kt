package com.keyvalue.siren.androidsdk.helper.client.callbacks

import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import org.json.JSONObject

/**
 * Callback interface for handling the result of marking a notification as read by ID.
 */
interface MarkAsReadByIdCallback {
    /**
     * Called when marking a notification as read by ID is successful.
     *
     * @param responseData The response data containing information about the operation.
     */
    fun onSuccess(responseData: MarkAsReadByIdResponseData?)

    /**
     * Called when an error occurs while marking a notification as read by ID.
     *
     * @param jsonObject The JSON object representing the error.
     */
    fun onError(jsonObject: JSONObject)
}
