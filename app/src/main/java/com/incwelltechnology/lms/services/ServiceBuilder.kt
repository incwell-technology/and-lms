package com.incwelltechnology.lms.services

import android.preference.PreferenceManager
import com.incwelltechnology.lms.App
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceBuilder {
    private const val URL = "http://192.168.1.104:8000/v1/api/"

    //create okHttp client
    private val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(getHttpLogger())
        .addInterceptor {
            val sharedPref= PreferenceManager.getDefaultSharedPreferences(App.get())
            val token=sharedPref.getString("TOKEN","12708097ff495e54aa8a15a4e830b886892f9e12")
            val request = it.request()
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Token $token")
                .build()
            val defaultRequest = request.newBuilder()
                .addHeader("Authorization", "Token $token")
                .build()
            if(token!=""){
                it.proceed(newRequest)
            }else{
                it.proceed(defaultRequest)
            }
        }
    //create Retrofit builder
    private val builder = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    //create instance of Retrofit
    private val retrofit: Retrofit = builder.build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }

    private fun getHttpLogger(): HttpLoggingInterceptor {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }
}