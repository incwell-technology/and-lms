package com.incwelltechnology.lms.data.network

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.LoginRequest
import com.incwelltechnology.lms.data.model.User
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
        operator fun invoke(): LmsApi {
            return Retrofit.Builder()
                .baseUrl("http://192.168.1.100:8000/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LmsApi::class.java)
        }
    }
}