package com.keyvalue.siren.androidsdk.utils.constants

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyvalue.siren.androidsdk.helper.client.BadgeStyle
import com.keyvalue.siren.androidsdk.helper.client.BadgeThemeProps
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
import com.keyvalue.siren.androidsdk.helper.customization.CustomStyles
import com.keyvalue.siren.androidsdk.helper.customization.Theme
import com.keyvalue.siren.androidsdk.helper.customization.ThemeProps

enum class SirenErrorTypes {
    /** Generic error. */
    ERROR,

    /** Configuration error. The method/parameters are incorrect or not supported. */
    CONFIG_ERROR,

    /** Network error. */
    NETWORK_ERROR,
}

enum class BulkUpdateType {
    MARK_AS_READ,
    MARK_AS_DELETED,
}

enum class TokenVerificationStatus {
    PENDING,
    SUCCESS,
    FAILED,
}

enum class SdkState {
    RESTART,
    NOT_RESTART,
}

enum class ThemeModeEnum {
    DARK,
    LIGHT,
}

enum class ThemeColorsEnum {
    PrimaryColor,
    HighlightedCardColor,
    TextColor,
    NeutralColor,
    BorderColor,
    DateColor,
    DeleteIcon,
    TimerIcon,
    ClearAllIcon,
    InfiniteLoader,
}

val COLORS =
    mapOf(
        ThemeModeEnum.LIGHT to
            mapOf(
                ThemeColorsEnum.PrimaryColor to Color(0xFFFA9874),
                ThemeColorsEnum.HighlightedCardColor to Color(0xFFFFECE5),
                ThemeColorsEnum.TextColor to Color(0xFF344054),
                ThemeColorsEnum.NeutralColor to Color(0xFFFFFFFF),
                ThemeColorsEnum.BorderColor to Color(0xFFD0D5DD),
                ThemeColorsEnum.DateColor to Color(0xFF667185),
                ThemeColorsEnum.DeleteIcon to Color(0xFF9882B3),
                ThemeColorsEnum.TimerIcon to Color(0xFF667185),
                ThemeColorsEnum.ClearAllIcon to Color(0xFF667185),
                ThemeColorsEnum.InfiniteLoader to Color(0xFF4D2E6B),
            ),
        ThemeModeEnum.DARK to
            mapOf(
                ThemeColorsEnum.PrimaryColor to Color(0xFFFA9874),
                ThemeColorsEnum.HighlightedCardColor to Color(0xFF2E2D30),
                ThemeColorsEnum.TextColor to Color(0xFFFFFFFF),
                ThemeColorsEnum.NeutralColor to Color(0xFF232326),
                ThemeColorsEnum.BorderColor to Color(0xFF344054),
                ThemeColorsEnum.DateColor to Color(0xFF98A2B3),
                ThemeColorsEnum.DeleteIcon to Color(0xFF98A2B3),
                ThemeColorsEnum.TimerIcon to Color(0xFF98A2B3),
                ThemeColorsEnum.ClearAllIcon to Color(0xFFD0D5DD),
                ThemeColorsEnum.InfiniteLoader to Color(0xFF4D2E6B),
            ),
    )

val defaultTheme =
    Theme(
        light =
            ThemeProps(
                colors =
                    ThemeColors(
                        primaryColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.PrimaryColor),
                        textColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.TextColor),
                        neutralColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.NeutralColor),
                        borderColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.BorderColor),
                        highlightedCardColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.HighlightedCardColor),
                        dateColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.DateColor),
                        deleteIcon = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.DeleteIcon),
                        timerIcon = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.TimerIcon),
                        clearAllIcon = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.ClearAllIcon),
                        infiniteLoader = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.InfiniteLoader),
                    ),
                badgeStyle =
                    BadgeThemeProps(
                        color = Color.Red,
                        textColor = Color.White,
                    ),
                windowHeader =
                    WindowHeaderThemeProps(
                        background = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.NeutralColor),
                        titleColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.TextColor),
                        headerActionColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.ClearAllIcon),
                        borderColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.BorderColor),
                    ),
                windowContainer =
                    WindowContainerThemeProps(
                        background = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.NeutralColor),
                    ),
                notificationCard =
                    NotificationCardThemeProps(
                        borderColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.BorderColor),
                        background = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.HighlightedCardColor),
                        titleColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.TextColor),
                        subTitleColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.TextColor),
                        descriptionColor = COLORS[ThemeModeEnum.LIGHT]?.get(ThemeColorsEnum.TextColor),
                    ),
            ),
        dark =
            ThemeProps(
                colors =
                    ThemeColors(
                        primaryColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.PrimaryColor),
                        textColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.TextColor),
                        neutralColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.NeutralColor),
                        borderColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.BorderColor),
                        highlightedCardColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.HighlightedCardColor),
                        dateColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.DateColor),
                        deleteIcon = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.DeleteIcon),
                        timerIcon = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.TimerIcon),
                        clearAllIcon = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.ClearAllIcon),
                        infiniteLoader = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.InfiniteLoader),
                    ),
                badgeStyle =
                    BadgeThemeProps(
                        color = Color.Red,
                        textColor = Color.White,
                    ),
                windowHeader =
                    WindowHeaderThemeProps(
                        background = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.NeutralColor),
                        titleColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.TextColor),
                        headerActionColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.ClearAllIcon),
                        borderColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.BorderColor),
                    ),
                windowContainer =
                    WindowContainerThemeProps(
                        background = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.NeutralColor),
                    ),
                notificationCard =
                    NotificationCardThemeProps(
                        borderColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.BorderColor),
                        background = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.HighlightedCardColor),
                        titleColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.TextColor),
                        subTitleColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.TextColor),
                        descriptionColor = COLORS[ThemeModeEnum.DARK]?.get(ThemeColorsEnum.TextColor),
                    ),
            ),
    )

val defaultCustomStyles =
    CustomStyles(
        notificationIcon =
            NotificationIconStyle(
                size = 100.dp,
            ),
        window =
            WindowStyle(
                width = Dp.Infinity,
                height = Dp.Infinity,
            ),
        windowHeader =
            WindowHeaderStyle(
                height = 50.dp,
                titleFontWeight = FontWeight.W600,
                titleSize = 18.sp,
                titlePadding = 0.dp,
            ),
        windowContainer =
            WindowContainerStyle(
                padding = 0.dp,
            ),
        notificationCard =
            NotificationCardStyle(
                padding = 12.dp,
                borderWidth = 0.2.dp,
                avatarSize = 40.dp,
                titleFontWeight = FontWeight.W600,
                subTitleFontWeight = FontWeight.W500,
                descriptionFontWeight = FontWeight.W400,
                titleSize = 14.sp,
                subTitleSize = 14.sp,
                descriptionSize = 14.sp,
                dateSize = 12.sp,
            ),
        badgeStyle =
            BadgeStyle(
                size = 40.dp,
                borderShape = RoundedCornerShape(50),
                textSize = 20.sp,
                top = 0,
                right = 0,
            ),
        deleteIcon =
            DeleteIconStyle(
                size = 18.dp,
            ),
        dateIcon =
            DateIconStyle(
                size = 18.dp,
            ),
        clearAllIcon =
            ClearAllIconStyle(
                size = 25.dp,
            ),
    )
