package com.incwelltechnology.lms.authenticationServices


import com.incwelltechnology.lms.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationService {
    @POST("login")
    fun userLogin(@Body login: Login): Call<LoginResponse>

    @GET("leaves")
    fun getLeaveToday(): Call<BaseResponse<Leave>>

    @GET("birthdays")
    fun getBirthday(): Call<BaseResponse<List<Birthday>>>

    @GET("holidays")
    fun getHoliday(): Call<BaseResponse<Holiday>>

    @GET("users")
    fun getEmployee(): Call<BaseResponse<List<Employee>>>

    @POST("leaves/create")
    fun createLeave(@Body leaveApply: LeaveCreate): Call<BaseResponse<LeaveCreate>>
}