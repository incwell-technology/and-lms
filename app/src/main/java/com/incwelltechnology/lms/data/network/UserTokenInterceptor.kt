package com.incwelltechnology.lms.data.network

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

class UserTokenInterceptor(context: Context): Interceptor {
    private val applicationContext=context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPref= PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val token=sharedPref.getString("TOKEN","acfd78729800353a484e70288f6d0777b81bfac7")
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Token $token")
            .build()
        val defaultRequest = request.newBuilder()
            .addHeader("Authorization", "Token $token")
            .build()
        if(token!=""){
            return chain.proceed(newRequest)
        }else{
            return chain.proceed(defaultRequest)
        }
    }
}