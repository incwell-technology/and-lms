package com.incwelltechnology.lms.services

import com.incwelltechnology.lms.model.User
import retrofit2.Call
import retrofit2.http.GET

interface AuthenticationService {

    @GET(value = "User")
    fun getUserList(): Call<List<User>>
}