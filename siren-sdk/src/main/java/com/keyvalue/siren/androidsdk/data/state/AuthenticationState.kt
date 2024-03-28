package com.keyvalue.siren.androidsdk.data.state

import org.json.JSONObject

class AuthenticationState(
    var errorResponse: JSONObject? = null,
    var isAuthenticated: Boolean? = null,
)
