package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.Link
import com.incwelltechnology.lms.data.model.Password
import com.incwelltechnology.lms.data.network.LmsApi
import retrofit2.Response

class ChangePasswordRepository(private val lmsApi: LmsApi) {
    suspend fun checkLink(link:String):Response<BaseResponse<Link>>{
        return lmsApi.checkLink(link)
    }

    suspend fun changePassword(newPass:String,confirmPass:String,userId:Int):Response<BaseResponse<Password>>{
        val password=Password(newPass,confirmPass)
        return lmsApi.changePassword(userId,password)
    }
}