package com.incwelltechnology.lms.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private const val URL="lms.incwelltechnology.com"

    //create okHttp client
    private val okHttp:OkHttpClient.Builder = OkHttpClient.Builder()

    //create Retrofit builder
    private  val builder=Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    //create instance of Retrofit
    private  val retrofit:Retrofit=builder.build()

    fun<T> buildService(serviceType:Class<T>):T{
        return retrofit.create(serviceType)
    }
}