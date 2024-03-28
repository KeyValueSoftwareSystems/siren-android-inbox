package com.keyvalue.siren.androidsdk.helper.customization

import androidx.compose.runtime.Composable
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.helper.client.BadgeStyle
import com.keyvalue.siren.androidsdk.helper.client.BadgeThemeProps
import com.keyvalue.siren.androidsdk.helper.client.CardProps
import com.keyvalue.siren.androidsdk.helper.client.ClearAllIconStyle
import com.keyvalue.siren.androidsdk.helper.client.DateIconStyle
import com.keyvalue.siren.androidsdk.helper.client.DeleteIconStyle
import com.keyvalue.siren.androidsdk.helper.client.NotificationCardStyle
import com.keyvalue.siren.androidsdk.helper.client.NotificationCardThemeProps
import com.keyvalue.siren.androidsdk.helper.client.NotificationIconStyle
import com.keyvalue.siren.androidsdk.helper.client.ThemeColors
import com.keyvalue.siren.androidsdk.helper.client.WindowContainerStyle
import com.keyvalue.siren.androidsdk.helper.client.WindowContainerThemeProps
import com.keyvalue.siren.androidsdk.helper.client.WindowHeaderStyle
import com.keyvalue.siren.androidsdk.helper.client.WindowHeaderThemeProps
import com.keyvalue.siren.androidsdk.helper.client.WindowStyle

data class Theme(
    val dark: ThemeProps? = null,
    val light: ThemeProps? = null,
)

data class CustomStyles(
    val notificationIcon: NotificationIconStyle? = null,
    val window: WindowStyle? = null,
    val windowHeader: WindowHeaderStyle? = null,
    val windowContainer: WindowContainerStyle? = null,
    val notificationCard: NotificationCardStyle? = null,
    val badgeStyle: BadgeStyle? = null,
    val deleteIcon: DeleteIconStyle? = null,
    val dateIcon: DateIconStyle? = null,
    val clearAllIcon: ClearAllIconStyle? = null,
)

data class ThemeProps(
    val colors: ThemeColors? = null,
    val badgeStyle: BadgeThemeProps? = null,
    val windowHeader: WindowHeaderThemeProps? = null,
    val windowContainer: WindowContainerThemeProps? = null,
    val notificationCard: NotificationCardThemeProps? = null,
)

/**
 * Properties used to customize the inbox icon.
 *
 * @property theme Theme object for custom styling of the badge.
 * @property customStyles Custom styles to apply to the icon.
 * @property notificationIcon Custom composable icon to be used as the notification icon.
 * @property darkMode Enables dark mode for the badge.
 * @property disabled Disable click handler of icon.
 * @property hideBadge Hides the badge displayed on the notification icon.
 */
data class SirenInboxIconProps(
    val theme: Theme? = null,
    val customStyles: CustomStyles? = null,
    val notificationIcon: @Composable (() -> Unit)? = null,
    var darkMode: Boolean? = false,
    val disabled: Boolean? = false,
    val hideBadge: Boolean? = false,
)

/**
 * Properties for configuring the appearance and behavior of the Siren inbox.
 *
 * @property theme Color theme object for custom styling.
 * @property customStyles Additional custom styles for the Siren inbox.
 * @property title Title of the notification window.
 * @property hideHeader Flag to hide or show the header.
 * @property hideClearAll Flag indicating whether to hide the "Clear All" button in the header.
 * @property darkMode Flag to enable dark mode.
 * @property cardProps Additional properties for customizing the notification cards.
 * @property listEmptyComponent Custom composable function to display when the notification list is empty.
 * @property customHeader Custom composable function to be displayed at the top of the inbox.
 * @property customFooter Custom composable function to be displayed below the inbox list.
 * @property customNotificationCard Custom composable function for rendering individual notification cards.
 * @property customLoader Custom composable function for loader to be displayed when the notification list is loading.
 * @property customErrorWindow Custom composable function for error window to be displayed when an error occurs.
 * @property itemsPerFetch Number of notifications to be fetched each time
 */
data class SirenInboxProps(
    val theme: Theme? = null,
    val customStyles: CustomStyles? = null,
    val title: String? = null,
    val hideHeader: Boolean? = false,
    val hideClearAll: Boolean? = false,
    var darkMode: Boolean? = false,
    val cardProps: CardProps? = null,
    val listEmptyComponent: (@Composable () -> Unit)? = null,
    val customFooter: (@Composable () -> Unit)? = null,
    val customHeader: (@Composable () -> Unit)? = null,
    val customNotificationCard: (@Composable (AllNotificationResponseData) -> Unit)? = null,
    val customLoader: (@Composable () -> Unit)? = null,
    val customErrorWindow: (@Composable () -> Unit)? = null,
    val itemsPerFetch: Int? = 20,
)
