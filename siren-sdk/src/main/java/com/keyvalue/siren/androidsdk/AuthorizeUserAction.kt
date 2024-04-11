package com.keyvalue.siren.androidsdk

import android.util.Log
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_TOKEN_VERIFICATION_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.SirenErrorTypes
import com.keyvalue.siren.androidsdk.utils.constants.TOKEN_VERIFICATION_FAILED
import com.keyvalue.siren.androidsdk.utils.constants.TokenVerificationStatus
import org.json.JSONObject

object AuthorizeUserAction {
    var authenticationStatus: TokenVerificationStatus = TokenVerificationStatus.FAILED

    fun authorizeUserAction(callback: (error: JSONObject?, status: TokenVerificationStatus) -> Unit) {
        if (authenticationStatus == TokenVerificationStatus.FAILED) {
            callback(
                JSONObject().put("type", SirenErrorTypes.ERROR)
                    .put("code", TOKEN_VERIFICATION_FAILED)
                    .put("message", ERROR_MESSAGE_TOKEN_VERIFICATION_FAILED),
                TokenVerificationStatus.FAILED
            )
        } else {
            callback(
                null,
                authenticationStatus
            )
        }
    }
}
