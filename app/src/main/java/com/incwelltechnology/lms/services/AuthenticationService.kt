package com.incwelltechnology.lms.services


import com.incwelltechnology.lms.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationService {
    @POST("login")
    fun userLogin(@Body login: Login): Call<LoginResponse>

    @GET("leave")
    fun getLeaveToday(): Call<BaseResponse<Leave>>

    @GET("birthday")
    fun getBirthday(): Call<BaseResponse<Birthday>>

    @GET("holiday")
    fun getHoliday(): Call<BaseResponse<Holiday>>
}