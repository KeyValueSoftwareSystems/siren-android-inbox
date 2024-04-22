# Siren Android Inbox

## Overview

The `siren-android-inbox` sdk is a comprehensive and customizable android UI kit for displaying and managing notifications. This documentation provides comprehensive information on how to install, configure, and use the sdk effectively.

## 1. Installation

You will need your sdk token for initializing your library.

1. Add the mavenCentral repository to your build file
```kotlin
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```
2. Add the dependency

```kotlin
dependencies {
    implementation("io.sirenapp:sirenapp-android-inbox:1.0.0")
}
```

## 2. Configuration
### 2.1 Initialization

Initialize `SirenSDK` in `MainActivity` by passing the context, token, recipientId and errorCallback

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

### 2.2 Configure notification icon

This composable function consists of a notification icon along with a badge to display the number of unviewed notifications.

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

#### Parameters for notification icon
Below are optional parameters available for the icon composable function:

Parameter | Description | Type | Default value |
--- | --- | --- | --- |
theme | Data class defining the theme configuration |  Theme | null |
customStyles | Data class defining the customizable styles |  CustomStyles | null |
notificationIcon | Option to use custom notification icon | @Composable (() -> Unit)? | null |
darkMode | Toggle to enable dark mode |  Boolean | false |
disabled | Toggle to disable click on icon |  Boolean | false |
hideBadge | Toggle to hide the badge displayed on the notification icon | Boolean | false |

### 2.3 Configure notification inbox

Inbox is a paginated list view for displaying notifications.

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

#### Siren Inbox parameters

Given below are the list of parameters of the Siren inbox composable function:

Parameter | Description | Type | Default value |
--- | --- | --- | --- |
theme | Data class defining the theme configuration |  Theme | null |
customStyles | Data class defining the customizable styles |  Theme | null |
title |  Title of the notification inbox |  String | "Notifications" |
darkMode | Toggle to enable dark mode |  Boolean | false |
cardProps | Props for customizing the notification cards | CardProps | null |
inboxHeaderProps | Props for customizing the header | InboxHeaderProps | null |
listEmptyComponent | Custom composable function for empty notification list | (@Composable () -> Unit) | null |
customFooter | Custom footer composable function | (@Composable () -> Unit) | null |
customCard | Custom notification card composable | (@Composable (AllNotificationResponseData) -> Unit) | null |
customLoader | Custom composable function to display the initial loading state | (@Composable () -> Unit) | null |
customErrorWindow | Custom error window | (@Composable () -> Unit) | null |
itemsPerFetch | Number of notifications fetch per api request (have a max cap of 50) | Int | 20

##### CardProps
```kotlin
data class CardProps(
    val hideAvatar: Boolean? = false,
    val disableAutoMarkAsRead: Boolean? = false,
    val onAvatarClick: ((AllNotificationResponseData) -> Unit)? = null,
    val hideDelete: Boolean? = false,
)
```
##### InboxHeaderProps
```kotlin
data class InboxHeaderProps(
    val hideHeader: Boolean? = false,
    val hideClearAll: Boolean? = false,
    val customHeader: (@Composable () -> Unit)? = null,
    val showBackButton: Boolean? = false,
    val backButton: (@Composable () -> Unit)? = null,
    val handleBackNavigation: (() -> Unit)? = null,
)
```
#### Theme customization

Here are the available theme options:


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

#### Style options

Here are the custom style options:

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

## 3. Functions

Utility functions for modifying notifications:

Functions | Parameters | Type | Description |
----------|------------|-------|------------|
markAsReadByDate | startDate | ISO date string | Sets the read status of notifications to true until the given date |
markAsRead | id | string | Set read status of a notification to true          |
deleteById |  id | string  | Delete a notification by id |
deleteByDate | startDate | ISO date string | Delete all notifications until given date |
markAllAsViewed | startDate | ISO date string |Sets the viewed status of notifications to true until the given date |
updateToken | userToken, recipientId | string | To change the tokens

Sample code:
```kotlin
import com.keyvalue.siren.androidsdk.helper.client.SirenSDK
import com.keyvalue.siren.androidsdk.helper.client.callbacks.ErrorCallback
import com.keyvalue.siren.androidsdk.data.model.MarkAsReadByIdResponseData
import com.keyvalue.siren.androidsdk.helper.client.callbacks.MarkAsReadByIdCallback
...
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
    fun markAsRead() {
        sirenSDK.markAsRead(
            id = "ID_VALUE",
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
```

## 4. Error codes

Given below are all possible error codes thrown by sdk.

Error code  | Description |
--- | --- |
TIMED_OUT  | Request timed out|
INVALID_TOKEN  | The token passed is invalid |
INVALID_RECIPIENT_ID  | The recipient id passed is invalid |
TOKEN_VERIFICATION_FAILED | Verification of the given tokens has failed |
GENERIC_API_ERROR  | Occurrence of an unexpected api error |

## Example

Here's a basic example to help you get started

```kotlin

package com.keyvalue.sirensampleapp

// Add all necessary imports
...
import com.keyvalue.siren.androidsdk.data.model.AllNotificationResponseData
import com.keyvalue.siren.androidsdk.data.model.DataStatus
import com.keyvalue.siren.androidsdk.helper.client.SirenSDK
import com.keyvalue.siren.androidsdk.helper.client.callbacks.ErrorCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenAllNotificationUpdateCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxCallback
import com.keyvalue.siren.androidsdk.helper.client.callbacks.SirenInboxIconCallback
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxIconProps
import com.keyvalue.siren.androidsdk.helper.customization.SirenInboxProps

    // replace with userToken and recipientId

    val sirenSDK =
        SirenSDK.getInstance(
            this,
            "YOUR_USER_TOKEN",
            "YOUR_RECIPIENT_ID",
            object : ErrorCallback {
                override fun onError(jsonObject: JSONObject) {
                }
            },
        )
        
    sirenSDK.SirenInboxIcon(
        SirenInboxIconProps(
            darkMode = true,
            notificationIcon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    tint = Color(236, 155, 112, 255),
                    modifier = Modifier.size(100.dp),
                )
            },
        ),
        object : SirenInboxIconCallback {
            override fun onError(jsonObject: JSONObject) {
                // onError
            }

            override fun onClick() {
                // onClick
            }
        },
    )

    sirenSDK.SirenInbox(
        SirenInboxProps(),
        object : SirenInboxCallback {
            override fun onCardClick(notificationItem: AllNotificationResponseData) {
                // onClick
            }

            override fun onError(jsonObject: JSONObject) {
                // onError
            }
        },
    )

    fun deleteNotification() {
        sirenSDK.deleteById(
            id = "ID_VALUE",
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

```