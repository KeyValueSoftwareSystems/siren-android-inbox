package com.keyvalue.siren.androidsdk.utils

import androidx.compose.ui.Modifier
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keyvalue.siren.androidsdk.helper.client.CombinedBadgeThemeProps
import com.keyvalue.siren.androidsdk.helper.client.CombinedNotificationCardThemeProps
import com.keyvalue.siren.androidsdk.helper.client.CombinedThemeProps
import com.keyvalue.siren.androidsdk.helper.client.CombinedWindowContainerThemeProps
import com.keyvalue.siren.androidsdk.helper.client.CombinedWindowHeaderThemeProps
import com.keyvalue.siren.androidsdk.helper.client.NotificationIconProps
import com.keyvalue.siren.androidsdk.helper.client.ThemeColors
import com.keyvalue.siren.androidsdk.helper.client.WindowThemeProps
import com.keyvalue.siren.androidsdk.helper.customization.CustomStyles
import com.keyvalue.siren.androidsdk.helper.customization.Theme
import com.keyvalue.siren.androidsdk.utils.constants.ThemeModeEnum
import com.keyvalue.siren.androidsdk.utils.constants.defaultCustomStyles
import com.keyvalue.siren.androidsdk.utils.constants.defaultTheme
import java.text.SimpleDateFormat
import java.util.Locale

object SirenSDKUtils {
    private inline fun <I, reified O> I.convert(): O {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<O>() {}.type)
    }

    fun generateElapsedTimeText(timeString: String): String {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val targetTime = dateFormat.parse(timeString)?.time ?: 0
        val millisecondsDiff = currentTime - targetTime

        val seconds = (millisecondsDiff / 1000).toInt()
        val minutes = (seconds / 60).toInt()
        val hours = (minutes / 60).toInt()
        val days = (hours / 24).toInt()
        val months = (days / 30).toInt()
        val years = (days / 365).toInt()

        return when {
            millisecondsDiff < 60000 -> "Just now"
            minutes < 60 -> if (minutes == 1) "1 minute ago" else "$minutes minutes ago"
            hours < 24 -> if (hours == 1) "1 hour ago" else "$hours hours ago"
            days < 30 -> if (days == 1) "1 day ago" else "$days days ago"
            months < 12 -> if (months == 1) "1 month ago" else "$months months ago"
            else -> if (years == 1) "1 year ago" else "$years years ago"
        }
    }

    fun addOneMillisecond(startTime: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val startDate = dateFormat.parse(startTime)

        // Add one millisecond
        startDate?.time = startDate?.time?.plus(1) ?: 0

        return dateFormat.format(startDate)
    }

    fun applyTheme(
        clientTheme: Theme?,
        customStyles: CustomStyles?,
        isDarkMode: Boolean,
    ): CombinedThemeProps {
        val modeAppliedClientTheme =
            when (if (isDarkMode) ThemeModeEnum.DARK else ThemeModeEnum.LIGHT) {
                ThemeModeEnum.DARK -> clientTheme?.dark
                ThemeModeEnum.LIGHT -> clientTheme?.light
            }
        val modeAppliedDefaultTheme =
            when (if (isDarkMode) ThemeModeEnum.DARK else ThemeModeEnum.LIGHT) {
                ThemeModeEnum.DARK -> defaultTheme.dark
                ThemeModeEnum.LIGHT -> defaultTheme.light
            }
        return CombinedThemeProps(
            colors =
                ThemeColors(
                    primaryColor =
                        modeAppliedClientTheme?.colors?.primaryColor
                            ?: modeAppliedDefaultTheme?.colors?.primaryColor,
                    textColor =
                        modeAppliedClientTheme?.colors?.textColor
                            ?: modeAppliedDefaultTheme?.colors?.textColor,
                    neutralColor =
                        modeAppliedClientTheme?.colors?.neutralColor
                            ?: modeAppliedDefaultTheme?.colors?.neutralColor,
                    borderColor =
                        modeAppliedClientTheme?.colors?.borderColor
                            ?: modeAppliedDefaultTheme?.colors?.borderColor,
                    highlightedCardColor =
                        modeAppliedClientTheme?.colors?.highlightedCardColor
                            ?: modeAppliedDefaultTheme?.colors?.highlightedCardColor,
                    dateColor =
                        modeAppliedClientTheme?.colors?.dateColor
                            ?: modeAppliedDefaultTheme?.colors?.dateColor,
                    deleteIcon =
                        modeAppliedClientTheme?.colors?.deleteIcon
                            ?: modeAppliedDefaultTheme?.colors?.deleteIcon,
                    timerIcon =
                        modeAppliedClientTheme?.colors?.timerIcon
                            ?: modeAppliedDefaultTheme?.colors?.timerIcon,
                    clearAllIcon =
                        modeAppliedClientTheme?.colors?.clearAllIcon
                            ?: modeAppliedDefaultTheme?.colors?.clearAllIcon,
                    infiniteLoader =
                        modeAppliedClientTheme?.colors?.infiniteLoader
                            ?: modeAppliedDefaultTheme?.colors?.infiniteLoader,
                ),
            notificationIcon =
                NotificationIconProps(
                    size =
                        customStyles?.notificationIcon?.size
                            ?: defaultCustomStyles.notificationIcon?.size,
                ),
            badgeStyle =
                CombinedBadgeThemeProps(
                    size =
                        customStyles?.badgeStyle?.size
                            ?: defaultCustomStyles.badgeStyle?.size,
                    borderShape =
                        customStyles?.badgeStyle?.borderShape
                            ?: defaultCustomStyles.badgeStyle?.borderShape,
                    color =
                        modeAppliedClientTheme?.badgeStyle?.color
                            ?: modeAppliedDefaultTheme?.badgeStyle?.color,
                    textColor =
                        modeAppliedClientTheme?.badgeStyle?.textColor
                            ?: modeAppliedDefaultTheme?.badgeStyle?.textColor,
                    textSize =
                        customStyles?.badgeStyle?.textSize
                            ?: defaultCustomStyles.badgeStyle?.textSize,
                    top =
                        customStyles?.badgeStyle?.top
                            ?: defaultCustomStyles.badgeStyle?.top,
                    right =
                        customStyles?.badgeStyle?.right
                            ?: defaultCustomStyles.badgeStyle?.right,
                ),
            window =
                WindowThemeProps(
                    width =
                        customStyles?.window?.width
                            ?: defaultCustomStyles.window?.width,
                    height =
                        customStyles?.window?.height
                            ?: defaultCustomStyles.window?.height,
                ),
            windowHeader =
                CombinedWindowHeaderThemeProps(
                    background =
                        modeAppliedClientTheme?.windowHeader?.background
                            ?: modeAppliedClientTheme?.colors?.neutralColor
                            ?: modeAppliedDefaultTheme?.windowHeader?.background,
                    height =
                        customStyles?.windowHeader?.height
                            ?: defaultCustomStyles.windowHeader?.height,
                    titleColor =
                        modeAppliedClientTheme?.windowHeader?.titleColor
                            ?: modeAppliedClientTheme?.colors?.textColor
                            ?: modeAppliedDefaultTheme?.windowHeader?.titleColor,
                    titleFontWeight =
                        customStyles?.windowHeader?.titleFontWeight
                            ?: defaultCustomStyles.windowHeader?.titleFontWeight,
                    titleSize =
                        customStyles?.windowHeader?.titleSize
                            ?: defaultCustomStyles.windowHeader?.titleSize,
                    titlePadding =
                        customStyles?.windowHeader?.titlePadding
                            ?: defaultCustomStyles.windowHeader?.titlePadding,
                    headerActionColor =
                        modeAppliedClientTheme?.windowHeader?.headerActionColor
                            ?: modeAppliedClientTheme?.colors?.textColor
                            ?: modeAppliedDefaultTheme?.windowHeader?.headerActionColor,
                    borderColor =
                        modeAppliedClientTheme?.windowHeader?.borderColor
                            ?: modeAppliedClientTheme?.colors?.borderColor
                            ?: modeAppliedDefaultTheme?.windowHeader?.borderColor,
                    clearAllIconSize =
                        customStyles?.clearAllIcon?.size
                            ?: defaultCustomStyles.clearAllIcon?.size,
                ),
            windowContainer =
                CombinedWindowContainerThemeProps(
                    background =
                        modeAppliedClientTheme?.windowContainer?.background
                            ?: modeAppliedClientTheme?.colors?.neutralColor
                            ?: modeAppliedDefaultTheme?.windowContainer?.background,
                    padding =
                        customStyles?.windowContainer?.padding
                            ?: defaultCustomStyles.windowContainer?.padding,
                ),
            notificationCard =
                CombinedNotificationCardThemeProps(
                    padding =
                        customStyles?.notificationCard?.padding
                            ?: defaultCustomStyles.notificationCard?.padding,
                    borderWidth =
                        customStyles?.notificationCard?.borderWidth
                            ?: defaultCustomStyles.notificationCard?.borderWidth,
                    borderColor =
                        modeAppliedClientTheme?.notificationCard?.borderColor
                            ?: modeAppliedDefaultTheme?.notificationCard?.borderColor,
                    background =
                        modeAppliedClientTheme?.notificationCard?.background
                            ?: modeAppliedClientTheme?.colors?.highlightedCardColor
                            ?: modeAppliedDefaultTheme?.notificationCard?.background,
                    avatarSize =
                        customStyles?.notificationCard?.avatarSize
                            ?: defaultCustomStyles.notificationCard?.avatarSize,
                    titleColor =
                        modeAppliedClientTheme?.notificationCard?.titleColor
                            ?: modeAppliedClientTheme?.colors?.textColor
                            ?: modeAppliedDefaultTheme?.notificationCard?.titleColor,
                    titleFontWeight =
                        customStyles?.notificationCard?.titleFontWeight
                            ?: defaultCustomStyles.notificationCard?.titleFontWeight,
                    titleSize =
                        customStyles?.notificationCard?.titleSize
                            ?: defaultCustomStyles.notificationCard?.titleSize,
                    subTitleColor =
                        modeAppliedClientTheme?.notificationCard?.subTitleColor
                            ?: modeAppliedClientTheme?.colors?.textColor
                            ?: modeAppliedDefaultTheme?.notificationCard?.subTitleColor,
                    subTitleSize =
                        customStyles?.notificationCard?.subTitleSize
                            ?: defaultCustomStyles.notificationCard?.subTitleSize,
                    subTitleFontWeight =
                        customStyles?.notificationCard?.subTitleFontWeight
                            ?: defaultCustomStyles.notificationCard?.subTitleFontWeight,
                    descriptionColor =
                        modeAppliedClientTheme?.notificationCard?.descriptionColor
                            ?: modeAppliedClientTheme?.colors?.textColor
                            ?: modeAppliedDefaultTheme?.notificationCard?.descriptionColor,
                    descriptionSize =
                        customStyles?.notificationCard?.descriptionSize
                            ?: defaultCustomStyles.notificationCard?.descriptionSize,
                    descriptionFontWeight =
                        customStyles?.notificationCard?.descriptionFontWeight
                            ?: defaultCustomStyles.notificationCard?.descriptionFontWeight,
                    dateColor =
                        modeAppliedClientTheme?.colors?.dateColor
                            ?: modeAppliedDefaultTheme?.colors?.dateColor
                            ?: modeAppliedClientTheme?.colors?.textColor,
                    dateSize =
                        customStyles?.notificationCard?.dateSize
                            ?: defaultCustomStyles.notificationCard?.dateSize,
                    dateIconSize =
                        customStyles?.dateIcon?.size
                            ?: defaultCustomStyles.dateIcon?.size,
                    deleteIconSize =
                        customStyles?.deleteIcon?.size
                            ?: defaultCustomStyles.deleteIcon?.size,
                ),
        )
    }

    fun reduceOneMillisecond(startTime: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val startDate = dateFormat.parse(startTime)

        // Add one millisecond
        startDate?.time = startDate?.time?.minus(1) ?: 0

        return dateFormat.format(startDate)
    }

    fun Modifier.conditional(
        condition: Boolean,
        modifier: Modifier.() -> Modifier,
    ): Modifier {
        return if (condition) {
            then(modifier(Modifier))
        } else {
            this
        }
    }
}
