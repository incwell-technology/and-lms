package com.incwelltechnology.lms.services


import com.incwelltechnology.lms.model.LoginResponse
import com.incwelltechnology.lms.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("login")
    fun userLogin(@Body user: User): Call<LoginResponse>
}