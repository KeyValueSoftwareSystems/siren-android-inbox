package com.keyvalue.siren.androidsdk.helper.client

import android.content.Context
import androidx.annotation.Keep
import com.keyvalue.siren.androidsdk.helper.SirenSDKCore
import com.keyvalue.siren.androidsdk.helper.client.callbacks.ErrorCallback

@Keep
class SirenSDK {
    @Keep
    companion object {
        @Keep
        @JvmStatic
        fun getInstance(
            context: Context,
            userToken: String,
            recipientId: String,
            callback: ErrorCallback,
        ): SirenSDKClient {
            return SirenSDKCore(
                userToken,
                recipientId,
                callback,
            )
        }
    }
}
