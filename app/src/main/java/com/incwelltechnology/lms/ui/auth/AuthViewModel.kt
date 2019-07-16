package com.incwelltechnology.lms.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.UserRepository
import com.incwelltechnology.lms.util.Coroutine

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    var username: String? = null
    var password: String? = null

    var authListener: AuthListener? = null

    fun onLoginButtonClick() {
        authListener!!.onStarted()
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            //failure
            authListener?.onFailure("Username or Password is Empty")
            return
        }
        //when credential fields are not empty or null
        Coroutine.main {
            val loginResponse= userRepository.userLogin(username!!,password!!)
            if(loginResponse.body()?.status==true){
                authListener?.onSuccess(loginResponse.body()?.data!!)
                Log.d("test","${loginResponse.body()!!.data}")
            }else{
                authListener?.onFailure(loginResponse.body()!!.error)
                Log.d("test", loginResponse.body()!!.error)
            }
        }
    }
}