package com.incwelltechnology.lms.data.network

import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.data.model.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import org.koin.core.KoinApplication
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LmsApi {

    @GET("leaves")
    suspend fun getLeaveToday(): Response<BaseResponse<List<Leave>>>

    @GET("birthdays")
    suspend fun getBirthday(): Response<BaseResponse<User_Birthday>>

    @GET("holidays")
    suspend fun getHoliday(): Response<BaseResponse<List<Holiday>>>

    @GET("leaves-request")
    suspend fun getLeaveRequest(): Response<BaseResponse<List<RequestLeave>>>

    @GET("users")
    suspend fun getEmployee(): Response<BaseResponse<List<Employee>>>

    @POST("leaves/create")
    suspend fun applyLeave(@Body leaveRequest: LeaveRequest): Response<BaseResponse<LeaveRequest>>

    @POST("compensation/create")
    suspend fun createCompensation(@Body compensationApply: Compensation): Response<BaseResponse<Compensation>>

    @Multipart
    @POST("users/change-photo/{userId}")
    suspend fun changeProfilePicture(
        @Path("userId") userId: Int,
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<ProfileImage>>

    @GET("notifications")
    suspend fun getNotifications(): Response<BaseResponse<List<Notifications>>>

    @POST("create-notice")
    suspend fun postNotice(@Body notice: Notice): Response<BaseResponse<Notice>>

    @POST("register")
    suspend fun createUser(@Body registration: Registration): Response<BaseResponse<Registration>>

    @POST("leaves-request/{leaveId}")
    suspend fun responseForLeaveRequest(@Path("leaveId") leaveId: Int, @Body leaveRequestResponse: LeaveRequestResponse): Response<BaseResponse<LeaveRequestResponse>>


    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            userTokenInterceptor: UserTokenInterceptor
        ): LmsApi {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(userTokenInterceptor)
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