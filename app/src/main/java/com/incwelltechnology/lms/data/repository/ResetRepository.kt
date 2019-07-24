package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.Email
import com.incwelltechnology.lms.data.network.LmsApi
import retrofit2.Response

class ResetRepository(private val lmsApi: LmsApi) {
    suspend fun submitEmail(mail:String):Response<BaseResponse<Email>>{
        val email=Email(mail)
        return lmsApi.verifyEmail(email)
    }
}