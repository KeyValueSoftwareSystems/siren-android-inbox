package com.keyvalue.siren.androidsdk

import com.keyvalue.siren.androidsdk.utils.constants.AUTHENTICATION_PENDING
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_AUTHENTICATION_PENDING
import com.keyvalue.siren.androidsdk.utils.constants.ERROR_MESSAGE_UNAUTHORIZED_OPERATION
import com.keyvalue.siren.androidsdk.utils.constants.SirenErrorTypes
import com.keyvalue.siren.androidsdk.utils.constants.TokenVerificationStatus
import com.keyvalue.siren.androidsdk.utils.constants.UNAUTHORIZED_OPERATION
import org.json.JSONObject

object AuthorizeUserAction {
    var authenticationStatus: TokenVerificationStatus = TokenVerificationStatus.FAILED

    fun authorizeUserAction(callback: (error: JSONObject?, status: TokenVerificationStatus) -> Unit) {
        if (authenticationStatus == TokenVerificationStatus.FAILED) {
            callback(
                JSONObject().put("type", SirenErrorTypes.ERROR)
                    .put("code", UNAUTHORIZED_OPERATION)
                    .put("message", ERROR_MESSAGE_UNAUTHORIZED_OPERATION),
                TokenVerificationStatus.FAILED,
            )
        } else if (authenticationStatus == TokenVerificationStatus.PENDING) {
            callback(
                JSONObject().put("type", SirenErrorTypes.ERROR)
                    .put("code", AUTHENTICATION_PENDING)
                    .put("message", ERROR_MESSAGE_AUTHENTICATION_PENDING),
                TokenVerificationStatus.FAILED,
            )
        } else {
            callback(
                null,
                authenticationStatus,
            )
        }
    }
}
