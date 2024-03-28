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
import com.keyvalue.siren.androidsdk.helper.client.SirenSDK
import com.keyvalue.siren.androidsdk.helper.client.SirenSDKClient
import com.keyvalue.siren.androidsdk.helper.client.callbacks.ErrorCallback
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
                "YOUR_USER_TOKEN",
                "YOUR_RECIPIENT_ID",
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
    }
}
