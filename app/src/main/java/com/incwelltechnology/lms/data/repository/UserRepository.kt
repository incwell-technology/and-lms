package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.LoginRequest
import com.incwelltechnology.lms.data.model.User
import com.incwelltechnology.lms.data.network.LmsApi
import com.incwelltechnology.lms.data.storage.SharedPref
import retrofit2.Response

class UserRepository(private val lmsApi: LmsApi) {

    fun saveCredential(key:String,credential:User){
        return SharedPref.save(key,credential)
    }
    fun deleteCredential(key: String){
        return SharedPref.delete(key)
    }
    fun checkCredential(key: String):Boolean{
        return SharedPref.check(key)
    }
    fun getCredential(key:String):User{
        return SharedPref.get(key)
    }

    suspend fun userLogin(username: String, password: String): Response<BaseResponse<User>> {
        val credential = LoginRequest(username, password)
        return lmsApi.userLogin(credential)
    }
}