package com.incwelltechnology.lms.util

import android.content.Context
import android.net.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ConnectionManager : ConnectivityManager.NetworkCallback() {

   var networkRequest: NetworkRequest = NetworkRequest.Builder()
       .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
       .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
       .build()


    fun enable(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        Log.d("test","onCapabilitiesChanged")
    }

    override fun onLost(network: Network?) {
        super.onLost(network)
        Log.d("test","onLost")
    }

    override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
        super.onLinkPropertiesChanged(network, linkProperties)
        Log.d("test","onLinkPropertiesChanged")
    }

    override fun onUnavailable() {
        super.onUnavailable()
        Log.d("test","onUnavailable")
    }

    override fun onLosing(network: Network?, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        Log.d("test","onLosing")
    }

    override fun onAvailable(network: Network?) {
        super.onAvailable(network)
        Log.d("test","onAvailable")

    }
}