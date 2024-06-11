package com.keyvalue.siren.androidsdk.core.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
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
    val thumbnailUrl = notification?.message?.thumbnailUrl
    val avatarContentDescription = "siren-notification-avatar-${notification?.id}"
    val thumbnailContentDescription = "siren-notification-thumbnail-${notification?.id}"
    val avatarModifier =
        Modifier
            .size(notificationCardStyle?.avatarSize!!)
            .clip(CircleShape)
            .conditional(cardProps?.onAvatarClick != null && notification != null) {
                clickable {
                    cardProps?.onAvatarClick?.let {
                        if (notification != null) {
                            it(notification)
                        }
                    }
                }
            }
            .semantics { contentDescription = "siren-notification-avatar-${notification?.id}" }

    val avatarDefaultPainter =
        painterResource(id = if (darkMode) R.drawable.avatar_dark else R.drawable.avatar_light)
    val thumbnailDefaultPainter =
        painterResource(id = if (darkMode) R.drawable.thumbnail_dark else R.drawable.thumbnail_light)

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
        ) {
            if (cardProps?.hideAvatar != true) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    if (avatarImageUrl.isNullOrEmpty()) {
                        Image(
                            painter = avatarDefaultPainter,
                            contentDescription = avatarContentDescription,
                            contentScale = ContentScale.FillBounds,
                            modifier = avatarModifier,
                        )
                    } else {
                        AsyncImage(
                            model = avatarImageUrl,
                            contentDescription = avatarContentDescription,
                            contentScale = ContentScale.FillBounds,
                            modifier = avatarModifier,
                        )
                    }
                }
            }
            Column(
                modifier =
                    Modifier
                        .weight(5f)
                        .padding(start = 3.dp),
            ) {
                Row {
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
                    if (cardProps?.hideDelete != true) {
                        if (cardProps?.deleteIcon != null) {
                            cardProps.deleteIcon.also {
                                it()
                            }
                        } else {
                            Box(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .padding(end = 6.dp),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "siren-notification-delete-${notification?.id}",
                                    tint = themeColors?.deleteIcon!!,
                                    modifier =
                                        Modifier
                                            .size(notificationCardStyle.deleteIconSize!!)
                                            .clickable { deleteNotificationCallback() }
                                            .semantics {
                                                contentDescription =
                                                    "siren-notification-delete-${notification?.id}"
                                            },
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                notification?.message?.let { message ->
                    message.subHeader?.let { subHeader ->
                        Text(
                            text = subHeader,
                            color = notificationCardStyle.subtitleColor!!,
                            fontWeight = notificationCardStyle.subtitleFontWeight!!,
                            fontSize = notificationCardStyle.subtitleSize!!,
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

                if (cardProps?.hideMediaThumbnail != true) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp)
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(25.dp))
                                .clickable {
                                    cardProps?.onMediaThumbnailClick?.let {
                                        if (notification != null) {
                                            it(notification)
                                        }
                                    }
                                },
                    ) {
                        val painter =
                            rememberAsyncImagePainter(
                                model = thumbnailUrl,
                                error = thumbnailDefaultPainter,
                            )

                        Image(
                            painter = painter,
                            contentDescription = thumbnailContentDescription,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
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
        }
    }
}

@Composable
fun borderShape(thickness: Dp): Shape {
    val lineThicknessInPixels =
        with(LocalDensity.current) {
            thickness.toPx()
        }
    return GenericShape { size, _ ->
        moveTo(0f, 0f)
        lineTo(0f, size.height)
        lineTo(lineThicknessInPixels, size.height)
        lineTo(lineThicknessInPixels, 0f)
    }
}
