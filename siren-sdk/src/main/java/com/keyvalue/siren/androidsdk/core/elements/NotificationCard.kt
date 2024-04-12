package com.keyvalue.siren.androidsdk.core.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.keyvalue.siren.androidsdk.R
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.helper.client.CardProps
import com.keyvalue.siren.androidsdk.helper.client.CombinedNotificationCardThemeProps
import com.keyvalue.siren.androidsdk.helper.client.ThemeColors
import com.keyvalue.siren.androidsdk.utils.SirenSDKUtils
import com.keyvalue.siren.androidsdk.utils.SirenSDKUtils.conditional

@Composable
fun NotificationCard(
    notification: AllNotificationResponseData?,
    cardProps: CardProps?,
    notificationCardStyle: CombinedNotificationCardThemeProps?,
    defaultCardClickCallback: () -> Unit,
    deleteNotificationCallback: () -> Unit,
    onCardClick: ((AllNotificationResponseData) -> Unit),
    themeColors: ThemeColors?,
    darkMode: Boolean,
) {
    val avatarImageUrl = notification?.message?.avatar?.imageUrl

    val painter =
        if (avatarImageUrl.isNullOrEmpty()) {
            painterResource(id = if (darkMode) R.drawable.avatar_dark else R.drawable.avatar_light)
        } else {
            rememberImagePainter(
                data = avatarImageUrl,
                builder = {
                    transformations(CircleCropTransformation())
                },
            )
        }

    val modifier =
        Modifier
            .conditional(notification?.isRead == false) {
                background(notificationCardStyle?.background!!)
            }

    Box(
        modifier =
            modifier
                .border(
                    width = notificationCardStyle?.borderWidth!!,
                    color = notificationCardStyle.borderColor!!,
                ),
    ) {
        val borderStroke =
            if (notification?.isRead == false) {
                BorderStroke(5.dp, themeColors?.primaryColor!!)
            } else {
                BorderStroke(1.dp, Color.Transparent)
            }

        Row(
            modifier =
                modifier
                    .border(
                        borderStroke,
                        shape = borderShape(thickness = if (notification?.isRead == false) 5.dp else 1.dp),
                    )
                    .clickable {
                        onCardClick.let {
                            if (cardProps?.disableAutoMarkAsRead != true) {
                                defaultCardClickCallback()
                            }
                            notification?.let { notification ->
                                it(
                                    notification,
                                )
                            }
                        }
                    }
                    .padding(notificationCardStyle.padding!!),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (cardProps?.hideAvatar != true) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(notificationCardStyle.avatarSize!!)
                            .clip(CircleShape)
                            .weight(1f),
                )
            } else {
                Spacer(
                    modifier = Modifier.size(notificationCardStyle.avatarSize!!).weight(1f),
                )
            }
            Column(
                modifier =
                    Modifier
                        .weight(5f)
                        .padding(start = 5.dp),
            ) {
                notification?.message?.let { message ->
                    message.header?.let { header ->
                        Text(
                            text = header,
                            color = notificationCardStyle.titleColor!!,
                            maxLines = 1,
                            fontWeight = notificationCardStyle.titleFontWeight!!,
                            fontSize = notificationCardStyle.titleSize!!,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                notification?.message?.let { message ->
                    message.subHeader?.let { subHeader ->
                        Text(
                            text = subHeader,
                            color = notificationCardStyle.descriptionColor!!,
                            maxLines = 1,
                            fontSize = notificationCardStyle.descriptionSize!!,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                notification?.message?.let { message ->
                    Text(
                        text = message.body,
                        color = notificationCardStyle.descriptionColor!!,
                        fontSize = notificationCardStyle.descriptionSize!!,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = "Clock Icon",
                        colorFilter = ColorFilter.tint(themeColors?.timerIcon!!),
                        modifier =
                            Modifier
                                .size(notificationCardStyle.dateIconSize!!)
                                .padding(end = 5.dp),
                    )
                    notification?.let { responseData ->
                        responseData.createdAt.let { createdAt ->
                            val elapsedTimeText = SirenSDKUtils.generateElapsedTimeText(createdAt)
                            Text(
                                text = elapsedTimeText,
                                color = themeColors.dateColor!!,
                                fontSize = notificationCardStyle.dateSize!!,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.W400,
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.weight(1f).padding(end = 6.dp), contentAlignment = Alignment.CenterEnd) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Delete icon",
                    tint = themeColors?.deleteIcon!!,
                    modifier =
                        Modifier
                            .size(notificationCardStyle.deleteIconSize!!)
                            .clickable { deleteNotificationCallback() },
                )
            }
        }
    }
}

@Composable
fun borderShape(thickness: Dp): Shape {
    val lineThicknessInPixels =
        with(LocalDensity.current) {
            thickness.toPx()
        }
    return GenericShape {
            size, _ ->
        moveTo(0f, 0f)
        lineTo(0f, size.height)
        lineTo(lineThicknessInPixels, size.height)
        lineTo(lineThicknessInPixels, 0f)
    }
}
