package com.incwelltechnology.lms.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.AppConstants.key
import com.incwelltechnology.lms.data.model.User
import com.incwelltechnology.lms.data.repository.UserRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    var isPresent:Boolean ?= false
    var user: User?= null
    fun sharedPreference(){
        isPresent=userRepository.checkCredential(key)
        if (isPresent==true){
            user=userRepository.getCredential(key)
        }
    }

    var username: String? = null
    var password: String? = null

    var authListener: AuthListener? = null

    fun onLoginButtonClick() {
        authListener!!.onStarted()
        when {
            username.isNullOrEmpty() -> {
                authListener?.onFailure(AppConstants.USERNAME_EMPTY, "Username is Empty")
                return
            }
            password.isNullOrEmpty() -> {
                authListener?.onFailure(AppConstants.PASSWORD_EMPTY, "Password is Empty")
                return
            }
            else -> {
                //when credential fields are not empty or null
                Coroutine.io {
                    try {
                        val loginResponse = userRepository.userLogin(username!!, password!!)
                        if (loginResponse.body()?.status == true) {

                            //saving credentials when login is successfull
                            userRepository.saveCredential(key,loginResponse.body()?.data!!)

                            withContext(Dispatchers.Main) {
                                authListener?.onSuccess(loginResponse.body()?.data!!)
                                Log.d("test", "${loginResponse.body()!!.data}")
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                authListener?.onFailure(AppConstants.OTHER_CASE, loginResponse.body()!!.error)
                                Log.d("test", loginResponse.body()!!.error)
                            }
                        }
                    } catch (e: NoInternetException) {
                        withContext(Dispatchers.Main) {
                            authListener?.onFailure(AppConstants.OTHER_CASE, e.message!!)
                        }
                    } catch (e: SocketTimeoutException) {
                        withContext(Dispatchers.Main) {
                            authListener?.onFailure(AppConstants.OTHER_CASE, "Something went wrong! Please try again later.")
                        }
                    }
                }
            }
        }
    }
    fun onLogoutButtonClicked(){
        userRepository.deleteCredential(key)
    }

}