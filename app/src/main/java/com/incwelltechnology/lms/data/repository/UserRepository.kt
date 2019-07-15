package com.incwelltechnology.lms.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.incwelltechnology.lms.data.model.LoginRequest
import com.incwelltechnology.lms.data.model.LoginResponse
import com.incwelltechnology.lms.data.network.LmsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    fun userLogin(username: String, password: String): LiveData<String> {
        val loginData= LoginRequest(username, password)
        val loginResponse = MutableLiveData<String>()
        LmsApi().userLogin(loginData)
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResponse.value = t.message
                }
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.body()?.status==true) {
                        loginResponse.value = response.body()?.data.toString()
                    } else {
                        loginResponse.value = response.body()?.error.toString()
                    }
                }
            })
        return loginResponse
    }
}