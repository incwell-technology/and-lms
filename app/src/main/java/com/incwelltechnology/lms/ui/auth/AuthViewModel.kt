package com.incwelltechnology.lms.ui.auth

import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.UserRepository

class AuthViewModel : ViewModel() {
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
        //success
        val loginResponse= UserRepository().userLogin(username!!,password!!)
        authListener?.onSuccess(loginResponse)

    }
}