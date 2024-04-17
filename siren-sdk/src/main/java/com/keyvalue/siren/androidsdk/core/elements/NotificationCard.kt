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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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
                )
                .semantics { contentDescription = "siren-notification-card-${notification?.id}" },
    ) {
        val borderStroke =
            if (notification?.isRead == false) {
                BorderStroke(4.dp, themeColors?.primaryColor!!)
            } else {
                BorderStroke(1.dp, Color.Transparent)
            }

        Row(
            modifier =
                modifier
                    .border(
                        borderStroke,
                        shape = borderShape(thickness = if (notification?.isRead == false) 4.dp else 1.dp),
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
                    contentDescription = "siren-notification-avatar-${notification?.id}",
                    modifier =
                        Modifier
                            .size(notificationCardStyle.avatarSize!!)
                            .clip(CircleShape)
                            .weight(1f)
                            .conditional(cardProps?.onAvatarClick != null && notification != null) {
                                clickable {
                                    cardProps?.onAvatarClick?.let {
                                        if (notification != null) {
                                            it(notification)
                                        }
                                    }
                                }
                            }
                            .semantics { contentDescription = "siren-notification-avatar-${notification?.id}" },
                )
            }
            Column(
                modifier =
                    Modifier
                        .weight(5f)
                        .padding(start = 8.dp),
            ) {
                notification?.message?.let { message ->
                    message.header?.let { header ->
                        Text(
                            text = header,
                            color = notificationCardStyle.titleColor!!,
                            fontWeight = notificationCardStyle.titleFontWeight!!,
                            fontSize = notificationCardStyle.titleSize!!,
                            maxLines = 2,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                notification?.message?.let { message ->
                    message.subHeader?.let { subHeader ->
                        Text(
                            text = subHeader,
                            color = notificationCardStyle.descriptionColor!!,
                            fontWeight = notificationCardStyle.subTitleFontWeight!!,
                            fontSize = notificationCardStyle.descriptionSize!!,
                            maxLines = 2,
                            lineHeight = TextUnit.Unspecified,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                notification?.message?.let { message ->
                    Text(
                        text = message.body,
                        color = notificationCardStyle.descriptionColor!!,
                        fontWeight = notificationCardStyle.descriptionFontWeight!!,
                        fontSize = notificationCardStyle.descriptionSize!!,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

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

            if (cardProps?.hideDelete != true) {
                if (cardProps?.deleteIcon != null) {
                    cardProps.deleteIcon.also {
                        it()
                    }
                } else {
                    Box(modifier = Modifier.weight(1f).padding(end = 6.dp), contentAlignment = Alignment.CenterEnd) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "siren-notification-delete-${notification?.id}",
                            tint = themeColors?.deleteIcon!!,
                            modifier =
                                Modifier
                                    .size(notificationCardStyle.deleteIconSize!!)
                                    .clickable { deleteNotificationCallback() }
                                    .semantics { contentDescription = "siren-notification-delete-${notification?.id}" },
                        )
                    }
                }
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
