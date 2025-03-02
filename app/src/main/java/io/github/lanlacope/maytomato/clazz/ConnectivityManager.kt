package io.github.lanlacope.maytomato.clazz

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun ConnectivityManager.bindMobileCommunication(): Boolean = suspendCancellableCoroutine { cont ->
    val request = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val isSuccess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                bindProcessToNetwork(network)
            } else {
                @Suppress("DEPRECATION")
                ConnectivityManager.setProcessDefaultNetwork(network)
            }
            cont.resume(isSuccess)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            cont.resume(false)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            unregisterNetworkCallback(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                bindProcessToNetwork(null)
            } else {
                @Suppress("DEPRECATION")
                ConnectivityManager.setProcessDefaultNetwork(null)
            }
        }
    }

    requestNetwork(request, callback)

    cont.invokeOnCancellation { unregisterNetworkCallback(callback) }
}