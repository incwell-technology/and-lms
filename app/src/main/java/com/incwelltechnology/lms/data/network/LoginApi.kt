package com.incwelltechnology.lms.data.network

import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.data.model.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginApi {
    @POST("login")
    suspend fun userLogin(@Body login: LoginRequest): Response<BaseResponse<User>>

    @POST("users/password-reset")
    suspend fun verifyEmail(@Body resetPassword: Email): Response<BaseResponse<Email>>

    @GET("users/reset/{link}")
    suspend fun checkLink(@Path("link") link: String): Response<BaseResponse<Link>>

    @POST("users/password-reset-done/{userId}")
    suspend fun changePassword(@Path("userId") userId: Int, @Body newPass: Password): Response<BaseResponse<Password>>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): LoginApi {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstants.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginApi::class.java)
        }
    }
}