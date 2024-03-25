package com.keyvalue.siren.androidsdk.presenter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.keyvalue.siren.androidsdk.R

abstract class BasePresenter(
    private var context: Context,
) {
    protected var baseURL = context.resources.getString(R.string.SIREN_BASE_URL)

    protected fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
