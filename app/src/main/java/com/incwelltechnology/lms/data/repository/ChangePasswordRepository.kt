package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.Link
import com.incwelltechnology.lms.data.model.Password
import com.incwelltechnology.lms.data.network.LoginApi
import retrofit2.Response

class ChangePasswordRepository(private val loginApi: LoginApi) {
    suspend fun checkLink(link:String):Response<BaseResponse<Link>>{
        return loginApi.checkLink(link)
    }

    suspend fun changePassword(newPass:String,confirmPass:String,userId:Int):Response<BaseResponse<Password>>{
        val password=Password(newPass,confirmPass)
        return loginApi.changePassword(userId,password)
    }
}