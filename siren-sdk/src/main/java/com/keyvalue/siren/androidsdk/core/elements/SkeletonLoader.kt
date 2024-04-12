package com.keyvalue.siren.androidsdk.core.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.keyvalue.siren.androidsdk.utils.SirenSDKUtils.conditional

val lightModeGradientsList =
    listOf(
        Color(0xFFE4E7EC),
        Color(0xFFF0F2F5),
    )

val darkModeGradientsList =
    listOf(
        Color(0xFF585858),
        Color(0xFF6E6E6E),
    )

@Composable
private fun GradientBlock(
    height: Dp,
    modifier: Modifier? = Modifier,
    isDarkMode: Boolean,
) {
    val gradientList = if (isDarkMode) darkModeGradientsList else lightModeGradientsList
    Box(
        modifier =
            (modifier ?: Modifier)
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                            gradientList,
                            startY = 0f,
                            endY = 200f,
                        ),
                ),
    )
}

@Composable
private fun SkeletonLoaderItem(isDarkMode: Boolean) {
    val gradientList = if (isDarkMode) darkModeGradientsList else lightModeGradientsList
    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .conditional(isDarkMode) {
                        background(Color(0xFF232326))
                    }
                    .padding(16.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    colors =
                                    gradientList,
                                    startY = 0f,
                                    endY = 200f,
                                ),
                        ),
            )
            Column(
                modifier =
                    Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                        .weight(1f),
            ) {
                GradientBlock(height = 20.dp, isDarkMode = isDarkMode)
                Spacer(modifier = Modifier.height(8.dp))
                GradientBlock(height = 20.dp, isDarkMode = isDarkMode)
                Spacer(modifier = Modifier.height(8.dp))
                GradientBlock(height = 40.dp, isDarkMode = isDarkMode)
                Spacer(modifier = Modifier.height(15.dp))
                Row {
                    Box(
                        modifier =
                            Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(
                                    brush =
                                        Brush.verticalGradient(
                                            colors =
                                            gradientList,
                                            startY = 0f,
                                            endY = 200f,
                                        ),
                                ),
                    )
                    GradientBlock(
                        height = 15.dp,
                        isDarkMode = isDarkMode,
                        modifier = Modifier.padding(start = 5.dp),
                    )
                }
            }
            Box(
                modifier =
                    Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    colors =
                                    gradientList,
                                    startY = 0f,
                                    endY = 200f,
                                ),
                        ),
            )
        }
        Box(modifier = Modifier.height(1.dp).fillMaxWidth().background(gradientList[0]))
    }
}

@Composable
fun SkeletonLoader(isDarkMode: Boolean? = false) {
    Column {
        repeat(6) {
            SkeletonLoaderItem(isDarkMode = isDarkMode ?: false)
        }
    }
}
