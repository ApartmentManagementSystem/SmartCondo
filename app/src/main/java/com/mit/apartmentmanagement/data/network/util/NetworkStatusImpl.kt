package com.mit.apartmentmanagement.data.network.util

import android.content.Context
import javax.inject.Inject

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Singleton


//@Singleton
class NetworkStatusImpl @Inject constructor(
    private val context: Context
) : NetworkStatus {

    override fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }
        } ?: false
    }
}