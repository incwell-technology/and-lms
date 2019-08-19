package com.incwelltechnology.lms.data.network

import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.data.model.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LmsApi {

    @GET("leaves")
    suspend fun getLeaveToday(): Response<BaseResponse<List<Leave>>>

    @GET("birthdays")
    suspend fun getBirthday():Response<BaseResponse<User_Birthday>>


    @GET("holidays")
    suspend fun getHoliday(): Response<BaseResponse<List<Holiday>>>

    @GET("users")
    suspend fun getEmployee() : Response<BaseResponse<List<Employee>>>

    @POST("leaves/create")
    suspend fun applyLeave(@Body leaveRequest: LeaveRequest):Response<BaseResponse<LeaveRequest>>

    @POST("compensation/create")
    suspend fun createCompensation(@Body compensationApply: Compensation): Response<BaseResponse<Compensation>>

    @Multipart
    @POST("users/change-photo/{userId}")
    suspend fun changeProfilePicture(
        @Path("userId") userId: Int,
        @Part image: MultipartBody.Part
    ):Response<BaseResponse<ProfileImage>>

    @GET("notifications")
    suspend fun getNotifications():Response<BaseResponse<List<Notifications>>>


    companion object {
        operator fun invoke(userTokenInterceptor: UserTokenInterceptor,networkConnectionInterceptor: NetworkConnectionInterceptor): LmsApi {

            val okHttpClient=OkHttpClient.Builder()
                .addInterceptor (userTokenInterceptor)
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstants.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LmsApi::class.java)
        }
    }
}