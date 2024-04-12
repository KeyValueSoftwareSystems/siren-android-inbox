package com.keyvalue.siren.androidsdk.presenter

import android.content.Context
import com.keyvalue.siren.androidsdk.data.managers.AuthenticationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class AuthenticationPresenter(
    context: Context,
    private var userToken: String,
    private var recipientId: String,
) : BasePresenter(context) {
    private var authenticationManager: AuthenticationManager? = null

    init {
        if (baseURL?.isNotEmpty() == true) {
            authenticationManager = AuthenticationManager(baseURL!!)
        }
    }

    fun verifyToken(callback: suspend (Boolean, JSONObject?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            authenticationManager?.verifyToken(userToken, recipientId)
            authenticationManager?.authenticationState?.collect {
                if (it?.isAuthenticated == true) {
                    callback(true, null)
                } else {
                    callback(false, it?.errorResponse)
                }
            }
        }
    }
}
