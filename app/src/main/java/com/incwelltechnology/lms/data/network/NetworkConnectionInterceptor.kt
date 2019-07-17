package com.incwelltechnology.lms.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.incwelltechnology.lms.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(context: Context):Interceptor{

    private val applicationContext=context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isNetworkAvailable())
            throw NoInternetException("Make sure you have Internet Connection!")
        return chain.proceed(chain.request())
    }
    private fun isNetworkAvailable():Boolean{
        val connectivityManager=applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it!=null && it.isConnected
        }
    }
}