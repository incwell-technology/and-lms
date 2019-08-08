package com.incwelltechnology.lms.data.network

import android.util.Log
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.data.storage.SharedPref
import okhttp3.Interceptor
import okhttp3.Response

class UserTokenInterceptor : Interceptor{
    lateinit var userToken:String
    override fun intercept(chain: Interceptor.Chain): Response {
        userToken=SharedPref.get(AppConstants.key).token
        Log.d("userToken",userToken)
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Token $userToken")
            .build()
        return chain.proceed(newRequest)
    }
}