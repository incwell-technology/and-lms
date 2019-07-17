package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.LoginRequest
import com.incwelltechnology.lms.data.model.User
import com.incwelltechnology.lms.data.network.LmsApi
import retrofit2.Response

class UserRepository(private val lmsApi: LmsApi) {
    suspend fun userLogin(username: String, password: String): Response<BaseResponse<User>> {
        val credential=LoginRequest(username, password)
        return lmsApi.userLogin(credential)
    }
}