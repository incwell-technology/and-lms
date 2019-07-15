package com.incwelltechnology.lms.data.network

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.LoginRequest
import com.incwelltechnology.lms.data.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LmsApi {

    @POST("login")
    fun userLogin(
        @Body login: LoginRequest
    ): Call<BaseResponse<User>>

    companion object{
        operator fun invoke():LmsApi{
            return Retrofit.Builder()
                .baseUrl("http://192.168.1.103:8000/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LmsApi::class.java)
        }
    }
}