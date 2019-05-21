package com.incwelltechnology.lms.services


import com.incwelltechnology.lms.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("login")
    fun getUserList(@Body user: User): Call<ResponseBody>
}