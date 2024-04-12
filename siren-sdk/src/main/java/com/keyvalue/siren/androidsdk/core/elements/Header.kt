package com.keyvalue.siren.androidsdk.core.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyvalue.siren.androidsdk.R
import com.keyvalue.siren.androidsdk.helper.client.ThemeColors
import com.keyvalue.siren.androidsdk.utils.constants.CLEAR_ALL_LABEL
import com.keyvalue.siren.androidsdk.utils.constants.DEFAULT_WINDOW_TITLE

@Composable
fun Header(
    title: String?,
    titleColor: Color,
    titleFontSize: TextUnit,
    titleFontWeight: FontWeight,
    titlePadding: Dp,
    headerActionColor: Color,
    borderBottomColor: Color,
    backgroundColor: Color,
    height: Dp,
    enableClearAll: Boolean,
    hideClearAll: Boolean,
    themeColors: ThemeColors?,
    clearAllIconSize: Dp,
    showBackButton: Boolean,
    backButton: (@Composable () -> Unit)?,
    handleBackNavigation: (() -> Unit)?,
    onClearAllClick: () -> Unit,
) {
    Column {
        Row(
            modifier =
                Modifier
                    .height(height)
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showBackButton) {
                    backButton?.let {
                        it()
                    }
                        ?: Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "notification icon with unread notifications indicator",
                            modifier =
                                Modifier
                                    .size(30.dp)
                                    .offset(x = (-5).dp)
                                    .clickable {
                                        if (handleBackNavigation != null) {
                                            handleBackNavigation()
                                        }
                                    },
                        )
                }
                Text(
                    text = title ?: DEFAULT_WINDOW_TITLE,
                    color = titleColor,
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    modifier = Modifier.padding(titlePadding),
                )
            }
            if (!hideClearAll) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .clickable(
                                enabled = enableClearAll,
                                onClick = onClearAllClick,
                            )
                            .alpha(if (enableClearAll) 1f else 0.4f),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.clear_all),
                        contentDescription = "Clear All Icon",
                        colorFilter = ColorFilter.tint(themeColors?.clearAllIcon!!),
                        modifier = Modifier.size(clearAllIconSize),
                    )
                    Text(
                        text = CLEAR_ALL_LABEL,
                        color = headerActionColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier =
                            Modifier
                                .padding(start = 3.dp, end = 7.dp),
                    )
                }
            }
        }
        Box(
            modifier =
                Modifier
                    .height(0.6.dp)
                    .fillMaxWidth()
                    .background(borderBottomColor),
        ) {}
    }
}
