package com.incwelltechnology.lms.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.UserRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    var username: String? = null
    var password: String? = null

    var authListener: AuthListener? = null

    fun onLoginButtonClick() {
        authListener!!.onStarted()
        when {
            username.isNullOrEmpty() -> {
                authListener?.onFailure(0, "Username is Empty")
                return
            }
            password.isNullOrEmpty() -> {
                authListener?.onFailure(1, "Password is Empty")
                return
            }
            else -> {
                //when credential fields are not empty or null
                Coroutine.main {
                    try {
                        val loginResponse = userRepository.userLogin(username!!, password!!)
                        if (loginResponse.body()?.status == true) {
                            withContext(Dispatchers.Main) {
                                authListener?.onSuccess(loginResponse.body()?.data!!)
                                Log.d("test", "${loginResponse.body()!!.data}")
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                authListener?.onFailure(2, loginResponse.body()!!.error)
                                Log.d("test", loginResponse.body()!!.error)
                            }
                        }
                    } catch (e: NoInternetException) {
                        withContext(Dispatchers.Main){
                            authListener?.onFailure(2, e.message!!)
                        }
                    }
                }
            }
        }
    }
}