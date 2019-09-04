package com.incwelltechnology.lms.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.incwelltechnology.lms.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private  val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain):Response {
        if (isNetworkAvailable()) {
            return chain.proceed(chain.request())
        } else{
            throw NoInternetException("Make sure you have Internet Connection!")
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
}