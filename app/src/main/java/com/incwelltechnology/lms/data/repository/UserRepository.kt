package com.incwelltechnology.lms.data.repository

import android.util.Log
import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.LoginRequest
import com.incwelltechnology.lms.data.model.User
import com.incwelltechnology.lms.data.model.Notice
import com.incwelltechnology.lms.data.network.LoginApi
import com.incwelltechnology.lms.data.storage.SharedPref
import retrofit2.Response

class UserRepository(private val loginApi: LoginApi) {

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

    suspend fun userLogin(username: String, password: String,fcm_token:String): Response<BaseResponse<User>> {
        val credential = LoginRequest(username, password, fcm_token)
        Log.d("response1","$credential")
        return loginApi.userLogin(credential)
    }

}