package com.keyvalue.siren.androidsdk.core.elements

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.keyvalue.siren.androidsdk.helper.client.ThemeColors
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HeaderTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun testHeaderRendering() {
        // Launch the Header composable with sample input values
        composeTestRule.setContent {
            Header(
                title = "Test Title",
                titleColor = Color.Black,
                titleFontSize = 20.sp,
                titleFontWeight = FontWeight.Bold,
                titlePadding = 8.dp,
                headerActionColor = Color.Red,
                borderBottomColor = Color.Gray,
                backgroundColor = Color.White,
                height = 56.dp,
                enableClearAll = true,
                hideClearAll = false,
                themeColors = ThemeColors(clearAllIcon = Color.Black),
                clearAllIconSize = 24.dp,
                showBackButton = true,
                backButton = null,
                handleBackNavigation = null,
                onClearAllClick = {},
            )
        }

        // Verify that the title text is displayed
        composeTestRule.onNodeWithText("Test Title").assertExists()

        // Verify that the clear all button is displayed
        composeTestRule.onNodeWithContentDescription("siren-header-clear-all").assertExists()
    }

    @Test
    fun testBackButtonNavigation() {
        var backNavigationTriggered = false

        // Launch the Header composable with back button and handleBackNavigation lambda
        composeTestRule.setContent {
            Header(
                title = "Test Title",
                titleColor = Color.Black,
                titleFontSize = 20.sp,
                titleFontWeight = FontWeight.Bold,
                titlePadding = 8.dp,
                headerActionColor = Color.Red,
                borderBottomColor = Color.Gray,
                backgroundColor = Color.White,
                height = 56.dp,
                enableClearAll = true,
                hideClearAll = false,
                themeColors = ThemeColors(clearAllIcon = Color.Black),
                clearAllIconSize = 24.dp,
                showBackButton = true,
                backButton = null,
                handleBackNavigation = {
                    backNavigationTriggered = true
                },
                onClearAllClick = {},
            )
        }

        // Click on the back button
        composeTestRule.onNodeWithContentDescription("siren-header-back").performClick()

        // Verify that handleBackNavigation lambda is triggered
        assert(backNavigationTriggered)
    }

    @Test
    fun testBackButtonNotDisplayed() {
        // Launch the Header composable with back button hidden
        composeTestRule.setContent {
            Header(
                title = "Test Title",
                titleColor = Color.Black,
                titleFontSize = 20.sp,
                titleFontWeight = FontWeight.Bold,
                titlePadding = 8.dp,
                headerActionColor = Color.Red,
                borderBottomColor = Color.Gray,
                backgroundColor = Color.White,
                height = 56.dp,
                enableClearAll = true,
                hideClearAll = false,
                themeColors = ThemeColors(clearAllIcon = Color.Black),
                clearAllIconSize = 24.dp,
                showBackButton = false,
                backButton = null,
                handleBackNavigation = null,
                onClearAllClick = {},
            )
        }

        // Verify that the back button is not displayed
        composeTestRule.onNodeWithContentDescription("siren-header-back").assertDoesNotExist()
    }

    @Test
    fun testClearAllButtonHidden() {
        // Launch the Header composable with clear all button hidden
        composeTestRule.setContent {
            Header(
                title = "Test Title",
                titleColor = Color.Black,
                titleFontSize = 20.sp,
                titleFontWeight = FontWeight.Bold,
                titlePadding = 8.dp,
                headerActionColor = Color.Red,
                borderBottomColor = Color.Gray,
                backgroundColor = Color.White,
                height = 56.dp,
                enableClearAll = false,
                hideClearAll = true,
                themeColors = ThemeColors(clearAllIcon = Color.Black),
                clearAllIconSize = 24.dp,
                showBackButton = true,
                backButton = null,
                handleBackNavigation = null,
                onClearAllClick = {},
            )
        }

        // Verify that the clear all button is not displayed
        composeTestRule.onNodeWithContentDescription("siren-header-clear-all").assertDoesNotExist()
    }
}
