package com.keyvalue.siren.androidsdk.helper.client.callbacks

import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import org.json.JSONObject

/**
 * Callback interface for handling events related to the Siren inbox.
 */
interface SirenInboxCallback {
    /**
     * Called when an error occurs while interacting with the Siren inbox.
     *
     * @param jsonObject The JSON object representing the error.
     */
    fun onError(jsonObject: JSONObject)

    /**
     * Called when a card in the Siren inbox is clicked.
     *
     * @param item The data associated with the clicked notification.
     */
    fun onCardClick(item: AllNotificationResponseData)
}
