package com.keyvalue.siren.androidsdk.core.elements

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.keyvalue.siren.androidsdk.utils.constants.INBOX_EMPTY_DESCRIPTION
import com.keyvalue.siren.androidsdk.utils.constants.INBOX_EMPTY_TITLE
import org.junit.Rule
import org.junit.Test

class InboxEmptyScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun testInboxEmptyScreen() {
        val backgroundColor = Color.White
        val titleColor = Color.Black
        val descriptionColor = Color.Gray
        val isDarkMode = false

        composeTestRule.setContent {
            val pullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = {})
            InboxEmptyScreen(
                pullRefreshState = pullRefreshState,
                backgroundColor = backgroundColor,
                titleColor = titleColor,
                descriptionColor = descriptionColor,
                isDarkMode = isDarkMode,
            )
        }

        // Verify that the image is displayed
        composeTestRule.onNodeWithContentDescription("siren-empty-state").assertExists()

        // Verify that the title text is displayed
        composeTestRule.onNodeWithText(INBOX_EMPTY_TITLE).assertExists()

        // Verify that the description text is displayed
        composeTestRule.onNodeWithText(INBOX_EMPTY_DESCRIPTION).assertExists()
    }
}
