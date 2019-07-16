package com.incwelltechnology.lms.data.network

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.LoginRequest
import com.incwelltechnology.lms.data.model.User
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LmsApi {
    @POST("login")
    suspend fun userLogin(
        @Body login: LoginRequest
    ): Response<BaseResponse<User>>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): LmsApi {
            val okHttpClient=OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://192.168.1.100:8000/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LmsApi::class.java)
        }
    }
}