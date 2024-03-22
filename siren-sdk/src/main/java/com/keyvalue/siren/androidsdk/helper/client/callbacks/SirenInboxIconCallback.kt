package com.keyvalue.siren.androidsdk.helper.client.callbacks

import org.json.JSONObject

/**
 * Callback interface for handling events related to Siren inbox icon.
 */
interface SirenInboxIconCallback {
    /**
     * Called when an error occurs.
     *
     * @param jsonObject The JSON object representing the error.
     */
    fun onError(jsonObject: JSONObject)

    /**
     * Called when the inbox icon is clicked.
     */
    fun onClick()
}
