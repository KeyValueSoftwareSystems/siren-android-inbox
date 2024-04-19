package com.keyvalue.siren.androidsdk.helper.client

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import com.keyvalue.siren.androidsdk.helper.client.callbacks.MarkAsReadByIdCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.MarkAsViewedCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenAllNotificationUpdateCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxIconCallback
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxIconProps
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxProps

@Keep
interface SirenSDKClient {
    /**
     * Marks a notification as read by its ID.
     *
     * @param notificationId The ID of the notification to mark as read.
     * @param callback The callback interface for handling the result of the operation.
     */
    fun markAsRead(
        notificationId: String,
        callback: MarkAsReadByIdCallback,
    )

    /**
     * Marks notifications as read up to a specified end date.
     *
     * @param untilDate The end date until which notifications should be marked as read.
     * @param callback The callback interface for handling the result of the operation.
     */
    fun markAsReadByDate(
        untilDate: String?,
        callback: SirenAllNotificationUpdateCallback,
    )

    /**
     * Marks notifications as viewed up to a specified end date.
     *
     * @param untilDate The end date until which notifications should be marked as viewed.
     * @param callback The callback interface for handling the result of the operation.
     */
    fun markAllAsViewed(
        untilDate: String?,
        callback: MarkAsViewedCallback,
    )

    /**
     * Deletes notifications up to a specified end date.
     *
     * @param untilDate The end date until which notifications should be deleted.
     * @param callback The callback interface for handling the result of the operation.
     */
    fun deleteByDate(
        untilDate: String?,
        callback: SirenAllNotificationUpdateCallback,
    )

    /**
     * Deletes a notification by its ID.
     *
     * @param id The ID of the notification to delete.
     * @param callback The callback interface for handling the result of the operation.
     */
    fun deleteById(
        id: String,
        callback: SirenAllNotificationUpdateCallback,
    )

    /**
     * Updates the user token and recipient ID.
     *
     * @param userToken The new user token.
     * @param recipientId The new recipient ID.
     */
    fun updateToken(
        userToken: String,
        recipientId: String,
    )

    /**
     * `SirenInboxIcon` is a composable function that displays an icon representing the entry point to view notifications.
     *
     * @param props Props for configuring the SirenInboxIcon composable.
     * @param callback The callback for handling events related to Siren inbox icon.
     */
    @Composable
    fun SirenInboxIcon(
        props: SirenInboxIconProps,
        callback: SirenInboxIconCallback,
    )

    /**
     * `SirenInbox` is a composable function that displays a list of notifications fetched from the Siren SDK.
     *
     * @param props  Props for configuring the SirenInbox composable.
     * @param callback The callback interface for handling events related to the Siren inbox.
     */
    @Composable
    fun SirenInbox(
        props: SirenInboxProps,
        callback: SirenInboxCallback,
    )
}
