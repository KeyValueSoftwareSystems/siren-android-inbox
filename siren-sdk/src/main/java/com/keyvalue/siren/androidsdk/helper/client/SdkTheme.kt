package com.keyvalue.siren.androidsdk.helper.client

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData

data class ThemeColors(
    val primaryColor: Color? = null,
    val textColor: Color? = null,
    val neutralColor: Color? = null,
    val borderColor: Color? = null,
    val highlightedCardColor: Color? = null,
    val dateColor: Color? = null,
    val deleteIcon: Color? = null,
    val timerIcon: Color? = null,
    val clearAllIcon: Color? = null,
    val infiniteLoader: Color? = null,
)

data class BadgeThemeProps(
    val color: Color? = null,
    val textColor: Color? = null,
)

data class WindowThemeProps(
    val width: Dp? = null,
    val height: Dp? = null,
)

data class WindowHeaderThemeProps(
    val background: Color? = null,
    val titleColor: Color? = null,
    val headerActionColor: Color? = null,
    val borderColor: Color? = null,
)

data class WindowContainerThemeProps(
    val background: Color? = null,
)

data class NotificationCardThemeProps(
    val borderColor: Color? = null,
    val background: Color? = null,
    val titleColor: Color? = null,
    val descriptionColor: Color? = null,
)

data class CardProps(
    val hideAvatar: Boolean? = false,
    val onAvatarClick: ((AllNotificationResponseData) -> Unit)? = null,
    val disableAutoMarkAsRead: Boolean? = false,
    val hideDelete: Boolean? = false,
)

data class InboxHeaderProps(
    val hideHeader: Boolean? = false,
    val hideClearAll: Boolean? = false,
    val customHeader: (@Composable () -> Unit)? = null,
    val showBackButton: Boolean? = false,
    val backButton: (@Composable () -> Unit)? = null,
    val handleBackNavigation: (() -> Unit)? = null,
)

data class NotificationIconProps(
    val size: Dp? = null,
)

data class NotificationIconStyle(
    val size: Dp? = null,
)

data class WindowStyle(
    val width: Dp? = null,
    val height: Dp? = null,
)

data class WindowHeaderStyle(
    val height: Dp? = null,
    val titleFontWeight: FontWeight? = null,
    val titleSize: TextUnit? = null,
    val closeIconSize: Dp? = null,
    val titlePadding: Dp? = null,
)

data class WindowContainerStyle(
    val padding: Dp? = null,
)

data class NotificationCardStyle(
    val padding: Dp? = null,
    val borderWidth: Dp? = null,
    val avatarSize: Dp? = null,
    val titleFontWeight: FontWeight? = null,
    val subTitleFontWeight: FontWeight? = null,
    val descriptionFontWeight: FontWeight? = null,
    val titleSize: TextUnit? = null,
    val descriptionSize: TextUnit? = null,
    val dateSize: TextUnit? = null,
)

data class BadgeStyle(
    val size: Dp? = null,
    val textSize: TextUnit? = null,
    val borderShape: Shape? = null,
    val top: Int? = null,
    val right: Int? = null,
)

data class DeleteIconStyle(
    val size: Dp? = null,
)

data class DateIconStyle(
    val size: Dp? = null,
)

data class ClearAllIconStyle(
    val size: Dp? = null,
)

data class CombinedThemeProps(
    val colors: ThemeColors? = null,
    val notificationIcon: NotificationIconProps? = null,
    val badgeStyle: CombinedBadgeThemeProps? = null,
    val window: WindowThemeProps? = null,
    val windowHeader: CombinedWindowHeaderThemeProps? = null,
    val windowContainer: CombinedWindowContainerThemeProps? = null,
    val notificationCard: CombinedNotificationCardThemeProps? = null,
)

data class CombinedBadgeThemeProps(
    val size: Dp? = null,
    val borderShape: Shape? = null,
    val color: Color? = null,
    val textColor: Color? = null,
    val textSize: TextUnit? = null,
    val top: Int? = null,
    val right: Int? = null,
)

data class CombinedWindowHeaderThemeProps(
    val background: Color? = null,
    val height: Dp? = null,
    val titleColor: Color? = null,
    val titleFontWeight: FontWeight? = null,
    val titleSize: TextUnit? = null,
    val headerActionColor: Color? = null,
    val titlePadding: Dp? = null,
    val borderColor: Color? = null,
    val clearAllIconSize: Dp? = null,
)

data class CombinedWindowContainerThemeProps(
    val background: Color? = null,
    val padding: Dp? = null,
)

data class CombinedNotificationCardThemeProps(
    val padding: Dp? = null,
    val borderWidth: Dp? = null,
    val borderColor: Color? = null,
    val background: Color? = null,
    val avatarSize: Dp? = null,
    val titleColor: Color? = null,
    val titleFontWeight: FontWeight? = null,
    val titleSize: TextUnit? = null,
    val subTitleFontWeight: FontWeight? = null,
    val descriptionColor: Color? = null,
    val descriptionSize: TextUnit? = null,
    val descriptionFontWeight: FontWeight? = null,
    val dateColor: Color? = null,
    val dateSize: TextUnit? = null,
    val dateIconSize: Dp? = null,
    val deleteIconSize: Dp? = null,
)
