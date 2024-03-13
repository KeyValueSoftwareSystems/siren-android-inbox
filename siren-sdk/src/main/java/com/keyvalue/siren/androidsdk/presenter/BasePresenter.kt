package com.keyvalue.siren.androidsdk.presenter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

abstract class BasePresenter {
    private var baseContext: Context? = null
    protected var baseURL = "https://api.dev.sirenapp.io/"

    protected fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            baseContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}
