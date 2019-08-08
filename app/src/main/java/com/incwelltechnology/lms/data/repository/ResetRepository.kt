package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.Email
import com.incwelltechnology.lms.data.network.LoginApi
import retrofit2.Response

class ResetRepository(private val loginApi: LoginApi) {
    suspend fun submitEmail(mail:String):Response<BaseResponse<Email>>{
        val email=Email(mail)
        return loginApi.verifyEmail(email)
    }
}