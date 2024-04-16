package com.keyvalue.siren.androidsdk.core.elements

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.Avatar
import com.keyvalue.siren.androidsdk.data.model.Message
import com.keyvalue.siren.androidsdk.helper.client.CardProps
import com.keyvalue.siren.androidsdk.helper.client.CombinedNotificationCardThemeProps
import com.keyvalue.siren.androidsdk.helper.client.ThemeColors
import org.junit.Rule
import org.junit.Test

class NotificationCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testNotificationCardRendering() {
        val notification =
            AllNotificationResponseData(
                createdAt = "2024-04-16T11:26:19.142+00:00",
                id = "1",
                isRead = false,
                message =
                    Message(
                        actionUrl = "https://example.com",
                        additionalData = null,
                        avatar = Avatar(imageUrl = "https://example.com/avatar.jpg", actionUrl = "https://example.com/avatar.jpg"),
                        body = "Body",
                        channel = "Channel",
                        header = "Header",
                        subHeader = "SubHeader",
                    ),
                requestId = "1",
            )

        val cardProps =
            CardProps(
                hideAvatar = false,
                disableAutoMarkAsRead = false,
            )

        val notificationCardStyle =
            CombinedNotificationCardThemeProps(
                padding = 16.dp,
                borderWidth = 1.dp,
                borderColor = Color.Gray,
                background = Color.White,
                avatarSize = 48.dp,
                titleColor = Color.Black,
                titleFontWeight = FontWeight.W600,
                titleSize = 16.sp,
                descriptionColor = Color.Gray,
                descriptionSize = 10.sp,
                dateColor = Color.Gray,
                dateSize = 10.sp,
                dateIconSize = 16.dp,
                deleteIconSize = 24.dp,
            )

        val themeColors =
            ThemeColors(
                primaryColor = Color.Blue,
                textColor = Color.Black,
                neutralColor = Color.Gray,
                borderColor = Color.Gray,
                highlightedCardColor = Color.White,
                dateColor = Color.Gray,
                deleteIcon = Color.Red,
                timerIcon = Color.Blue,
                clearAllIcon = Color.Green,
                infiniteLoader = Color.Magenta,
            )

        composeTestRule.setContent {
            NotificationCard(
                notification = notification,
                cardProps = cardProps,
                notificationCardStyle = notificationCardStyle,
                defaultCardClickCallback = {},
                deleteNotificationCallback = {},
                onCardClick = {},
                themeColors = themeColors,
                darkMode = false,
            )
        }

        // Verify that the image is displayed
        composeTestRule.onNodeWithContentDescription("siren-notification-avatar-1").assertExists()

        // Verify that the header text is displayed
        composeTestRule.onNodeWithText("Header").assertExists()

        // Verify that the subheader text is displayed
        composeTestRule.onNodeWithText("SubHeader").assertExists()

        // Verify that the body text is displayed
        composeTestRule.onNodeWithText("Body").assertExists()

        // Verify that the clock icon is displayed
        composeTestRule.onNodeWithContentDescription("Clock Icon").assertExists()

        // Verify that the delete icon is displayed
        composeTestRule.onNodeWithContentDescription("siren-notification-delete-1").assertExists()
    }
}
