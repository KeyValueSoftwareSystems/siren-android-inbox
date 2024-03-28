package com.keyvalue.siren.androidsdk.helper.client.callbacks

import com.keyvalue.siren.androidsdk.data.model.DataStatus
import org.json.JSONObject

/**
 * Callback interface for handling the result of updating a set of notifications.
 */
interface SirenAllNotificationUpdateCallback {
    /**
     * Called when the operation is successful.
     *
     * @param dataStatus The status of the operation.
     */
    fun onSuccess(dataStatus: DataStatus?)

    /**
     * Called when an error occurs during the operation.
     *
     * @param jsonObject The JSON object representing the error.
     */
    fun onError(jsonObject: JSONObject)
}
