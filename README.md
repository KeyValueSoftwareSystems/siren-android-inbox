<div>
    <img width="50px" style="float:left;padding-right:12px;" src="https://app.dev.sirenapp.io/assets/Siren-b2f89b52.svg" >
    <H1>Siren Android Inbox</H1>
</div>

## Table of Contents

<!-- MarkdownTOC -->

- [Overview](#overview)
- [Quick Start Guide](#quick-start-guide)
    - [Install Siren SDK](#install-siren-sdk)
        - [Using Android Studio](#using-android-studio)
    - [Siren Inbox Icon](#siren-inbox-icon)
    - [Siren Inbox](#siren-inbox)
    - [Error Codes](#error-codes)
    - [Complete Code Example](#complete-code-example)
- [I want to know more!](#i-want-to-know-more)

<!-- /MarkdownTOC -->


<a name="introduction"></a>

## Overview

The siren-android-inbox sdk is a comprehensive and customizable android UI kit for displaying and managing notifications. This documentation provides comprehensive information on how to install, configure, and use the sdk effectively.

## Quick Start Guide

### Install Siren SDK

You will need your sdk token for initializing your library.

### Using Android Studio

1. If this is your first time using Android Studio, Install Android Studio and dependencies
   from [Android Studio's Website](https://developer.android.com/studio). And start a new project.
2. Add the JitPack repository to your build file

```kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

3. Add the dependency

```kotlin
dependencies {
    implementation 'com.github.keyvalue:siren-android-inbox:1.0.0'
}
```

## Initialize SirenSDK

Import and initialize `SirenSDK` in `MainActivity`, passing the context, token, recipientId and errorCallback

```kotlin
import com.keyvalue.siren.androidsdk.helper.client.SirenSDK

class MainActivity {
    ...

    val sirenSDK =
        SirenSDK.getInstance(
            context = applicationContext,
            token = "USER_TOKEN",
            recipientId = "RECIPIENT_ID",
            object : ErrorCallback {
                override fun onError(jsonObject: JSONObject) {
                }
            },
        )
    ...
}
```

### Siren Inbox Icon

This composable function includes a customizable notification icon and a badge for indicating the number of un-viewed notifications in the user interface.

```kotlin
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxIconProps

sirenSDK.SirenInboxIcon(
    SirenInboxIconProps(
        theme = customTheme,
        darkMode = true,
        customStyles = customStyles,
        notificationIcon = CustomIcon(),
    ),
    object : SirenInboxIconCallback {
        override fun onError(jsonObject: JSONObject) {
        }

        override fun onClick() {
        }
    },
)

```

#### Siren Inbox Icon Props

Given below are the list of parameters of Icon composable function.

Parameter | Description | Type | Default value |
--- | --- | --- | --- |
theme | Theme Data class defining the theme configuration options |  Theme | null |
customStyles | Custom style Props Data class defining the customizable styling options |  CustomStyles | null |
notificationIcon | Option to use custom notification Icon | @Composable (() -> Unit)? | null |
darkMode | Flag to enable dark mode |  Boolean | false |
disabled | Flag to disable click handler of icon |  Boolean | false |

### Siren Inbox

Siren Inbox is a paginated list view for displaying notifications.

```kotlin
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxProps
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData

sirenSDK.SirenInbox(
    SirenInboxProps(
        theme = customTheme,
        title = "Notifications",
        hideHeader = false,
        darkMode = true,
    ),
    object : SirenInboxCallback {
        override fun onCardClick(notificationItem: AllNotificationResponseData) {
        }

        override fun onError(jsonObject: JSONObject) {
        }
    },
)

```

#### Siren Inbox Props

Given below are the list of parameters of the Siren inbox composable function.

Parameter | Description | Type | Default value |
--- | --- | --- | --- |
theme | Theme Data class defining the theme configuration options |  Theme | null |
customStyles | Custom styling configuration options |  Theme | null |
title |  Title of the notification window |  String | "Notifications" |
hideHeader | Flag to hide or show the header |  Boolean | false |
hideClearAll | Flag to hide or show the clear all button in header |  Boolean | false |
darkMode | Flag to enable dark mode |  Boolean | false |
cardProps | Props for customizing the notification cards | CardProps | null |
listEmptyComponent | Custom composable function to display when the notification list is empty | (@Composable () -> Unit) | null |
customHeader | Custom header component | (@Composable () -> Unit) | null |
customFooter | Custom footer component | (@Composable () -> Unit) | null |
customNotificationCard | Custom function for rendering notification cards | (@Composable (AllNotificationResponseData) -> Unit) | null |
customLoader | Custom composable function for displaying loader | (@Composable () -> Unit) | null |
customErrorWindow | Custom composable function for displaying error window | (@Composable () -> Unit) | null |
itemsPerFetch | Number of notifications to be fetched each time | Int | 20

#### Theming options

Customize the theme configuration consisting of various theme properties such as colors, badge styles, window header styles, window container styles, and notification card styles.

```kotlin
import androidx.compose.ui.graphics.Color

data class Theme(
    val dark: ThemeProps? = null,
    val light: ThemeProps? = null,
)

data class ThemeProps(
    val colors: ThemeColors? = null,
    val badgeStyle: BadgeThemeProps? = null,
    val windowHeader: WindowHeaderThemeProps? = null,
    val windowContainer: WindowContainerThemeProps? = null,
    val notificationCard: NotificationCardThemeProps? = null,
)

data class ThemeColors(
    val primaryColor: Color? = null,
    val textColor: Color? = null,
    val neutralColor: Color? = null,
    val borderColor: Color? = null,
    val highlightedCardColor: Color? = null,
    val dateColor: Color? = null,
    val deleteIcon: Color? = null,
    val timerIcon: Color? = null,
    val clearAllIcon: Color? = null,
    val infiniteLoader: Color? = null,
)

data class BadgeThemeProps(
    val color: Color? = null,
    val textColor: Color? = null,
)

data class WindowHeaderThemeProps(
    val background: Color? = null,
    val titleColor: Color? = null,
    val headerActionColor: Color? = null,
    val borderColor: Color? = null,
)

data class WindowContainerThemeProps(
    val background: Color? = null,
)

data class NotificationCardThemeProps(
    val borderColor: Color? = null,
    val background: Color? = null,
    val titleColor: Color? = null,
    val descriptionColor: Color? = null,
)
```

#### Styling options

Customize the styling configurations for various UI elements such as notification icons, windows, window headers, window containers, notification cards, badges, delete icon, date icon, and clear all icon.

```js
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.graphics.Shape

data class CustomStyles(
    val notificationIcon: NotificationIconStyle? = null,
    val window: WindowStyle? = null,
    val windowHeader: WindowHeaderStyle? = null,
    val windowContainer: WindowContainerStyle? = null,
    val notificationCard: NotificationCardStyle? = null,
    val badgeStyle: BadgeStyle? = null,
    val deleteIcon: DeleteIconStyle? = null,
    val dateIcon: DateIconStyle? = null,
    val clearAllIcon: ClearAllIconStyle? = null,
)

data class NotificationIconStyle(
    val size: Dp? = null,
)

data class WindowStyle(
    val width: Dp? = null,
    val height: Dp? = null,
)

data class WindowHeaderStyle(
    val height: Dp? = null,
    val titleFontWeight: FontWeight? = null,
    val titleSize: TextUnit? = null,
    val closeIconSize: Dp? = null,
    val titlePadding: Dp? = null,
)

data class WindowContainerStyle(
    val padding: Dp? = null,
)

data class NotificationCardStyle(
    val padding: Dp? = null,
    val borderWidth: Dp? = null,
    val avatarSize: Dp? = null,
    val titleFontWeight: FontWeight? = null,
    val titleSize: TextUnit? = null,
    val descriptionSize: TextUnit? = null,
    val dateSize: TextUnit? = null,
)

data class BadgeStyle(
    val size: Dp? = null,
    val textSize: TextUnit? = null,
    val borderShape: Shape? = null,
    val top: Int? = null,
    val right: Int? = null,
)

data class DeleteIconStyle(
    val size: Dp? = null,
)

data class DateIconStyle(
    val size: Dp? = null,
)

data class ClearAllIconStyle(
    val size: Dp? = null,
)

```

### Functions

These are the functions for modifying notifications.

Function | Parameters |
--- | --- |
markAsRead | notificationId, callback |
markNotificationsAsReadByDate | untilDate, callback |
markNotificationsAsViewed | untilDate, callback |
deleteNotificationsByDate | untilDate, callback |
deleteNotification | notificationId, callback |
updateToken | userToken, recipientId

### Error codes

Given below are all possible error codes thrown by sdk.

Error code | Message | Description |
--- | --- | --- |
TIMED_OUT | Timed out | Request timed out|
INVALID_TOKEN | Invalid token | Invalid token |
INVALID_RECIPIENT_ID | Invalid recipient id | Invalid recipient id |
TOKEN_VERIFICATION_FAILED | Token verification failed | This operation requires a valid token |
GENERIC_API_ERROR | Generic api error | Service is not available |

### Complete Code Example

Here's a runnable code example that covers everything in this quick start guide.

```kotlin

package com.keyvalue.sirensampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.helper.client.BadgeThemeProps
import com.keyvalue.siren.androidsdk.data.model.DataStatus
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import com.keyvalue.siren.androidsdk.data.model.MarkAsViewedResponseData
import com.keyvalue.siren.androidsdk.helper.client.SirenSDK
import com.keyvalue.siren.androidsdk.helper.client.SirenSDKClient
import com.keyvalue.siren.androidsdk.helper.client.callbacks.ErrorCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.MarkAsReadByIdCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.MarkAsViewedCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenAllNotificationUpdateCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxIconCallback
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxIconProps
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxProps
import com.keyvalue.siren.androidsdk.helper.customization.Theme
import com.keyvalue.siren.androidsdk.helper.customization.ThemeProps
import com.keyvalue.sirensampleapp.ui.theme.SirenSampleAppTheme
import org.json.JSONObject

@Composable
fun NotificationIcon(
    sirenSDK: SirenSDKClient,
    navController: NavController,
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        sirenSDK.SirenInboxIcon(
            SirenInboxIconProps(
                theme =
                Theme(
                    dark =
                    ThemeProps(
                        badgeStyle =
                        BadgeThemeProps(
                            color = Color.Red,
                            textColor = Color.White,
                        ),
                    ),
                ),
                darkMode = true,
                notificationIcon = {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "notification icon with unread notifications indicator",
                        tint = Color(236, 155, 112, 255),
                        modifier = Modifier.size(100.dp),
                    )
                },
            ),
            object : SirenInboxIconCallback {
                override fun onError(jsonObject: JSONObject) {
                }

                override fun onClick() {
                    navController.navigate("notification_window")
                }
            },
        )
    }
}

@Composable
fun NotificationWindow(
    sirenSDK: SirenSDKClient,
    navController: NavController,
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        sirenSDK.SirenInbox(
            SirenInboxProps(),
            object : SirenInboxCallback {
                override fun onCardClick(notificationItem: AllNotificationResponseData) {
                }

                override fun onError(jsonObject: JSONObject) {
                }
            },
        )
    }
}

@Composable
fun MyApp(sirenSDK: SirenSDKClient) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "notification_icon") {
        composable("notification_icon") {
            NotificationIcon(sirenSDK, navController)
        }
        composable("notification_window") {
            NotificationWindow(sirenSDK, navController)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // replace with userToken and recipientId

        val sirenSDK =
            SirenSDK.getInstance(
                this,
                "userToken",
                "recipientId",
                object : ErrorCallback {
                    override fun onError(jsonObject: JSONObject) {
                    }
                },
            )
        setContent {
            SirenSampleAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        MyApp(sirenSDK)
                    }
                }
            }
        }

        fun markAsRead() {
            sirenSDK.markAsRead(
                notificationId = "notificationId",
                callback = object : MarkAsReadByIdCallback {
                    override fun onSuccess(responseData: MarkAsReadByIdResponseData?) {
                        // onSuccess
                    }

                    override fun onError(jsonObject: JSONObject) {
                        // onError
                    }
                }
            )
        }

        fun markNotificationsAsReadByDate() {
            sirenSDK.markNotificationsAsReadByDate(
                startDate = "startDate",
                callback = object : SirenAllNotificationUpdateCallback {
                    override fun onSuccess(dataStatus: DataStatus?) {
                        // onSuccess
                    }

                    override fun onError(jsonObject: JSONObject) {
                        // onError
                    }

                }
            )
        }

        fun markNotificationsAsViewed() {
            sirenSDK.markNotificationsAsViewed(
                startDate = "startDate",
                callback = object : MarkAsViewedCallback {
                    override fun onSuccess(responseData: MarkAsViewedResponseData) {
                        // onSuccess
                    }

                    override fun onError(jsonObject: JSONObject) {
                        // onError
                    }

                }
            )
        }

        fun deleteNotificationsByDate() {
            sirenSDK.deleteNotificationsByDate(
                startDate = "startDate",
                callback = object : SirenAllNotificationUpdateCallback {
                    override fun onSuccess(dataStatus: DataStatus?) {
                        // onSuccess
                    }

                    override fun onError(jsonObject: JSONObject) {
                        // onError
                    }

                }
            )
        }

        fun deleteNotification() {
            sirenSDK.deleteNotification(
                notificationId = "notificationId",
                callback = object : SirenAllNotificationUpdateCallback {
                    override fun onSuccess(dataStatus: DataStatus?) {
                        // onSuccess
                    }

                    override fun onError(jsonObject: JSONObject) {
                        // onError
                    }

                }
            )
        }

        fun updateToken() {
            sirenSDK.updateToken(
                userToken = "userToken",
                recipientId = "recipientId"
            )
        }


    }
}


```

### I want to know more!

No worries, here are some links that you will find useful:

* **[Advanced Kotlin-Android Guide](https://developer.android.com/kotlin)**