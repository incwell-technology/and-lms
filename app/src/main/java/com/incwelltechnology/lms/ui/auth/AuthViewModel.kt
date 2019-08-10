package com.incwelltechnology.lms.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.iid.FirebaseInstanceId
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.AppConstants.key
import com.incwelltechnology.lms.data.model.User
import com.incwelltechnology.lms.data.repository.UserRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
import java.net.SocketTimeoutException

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    var fcmToken: String = ""
    var isPresent: Boolean? = false
    var user: User? = null
    var username: String? = null
    var password: String? = null
    var authListener: AuthListener? = null

    fun sharedPreference() {
        isPresent = userRepository.checkCredential(key)
        if (isPresent == true) {
            user = userRepository.getCredential(key)
        }
    }

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
                try {
                    //retrieve current fcm token
                    FirebaseInstanceId
                        .getInstance()
                        .instanceId
                        .addOnCompleteListener {
                            if (!it.isSuccessful) {
                                return@addOnCompleteListener
                            }
                            Coroutine.main {
                                fcmToken = it.result!!.token
                                val loginResponse = userRepository.userLogin(username!!, password!!, fcmToken)
                                if (loginResponse.body()?.status == true) {
                                    //saving credentials when login is successfull
                                    userRepository.saveCredential(key, loginResponse.body()?.data!!)
                                    authListener?.onSuccess(loginResponse.body()?.data!!)
                                } else {
                                    authListener?.onFailure(AppConstants.OTHER_CASE, "Invalid Credentials!")
                                }
                            }
                        }
                } catch (e: NoInternetException) {
                    authListener?.onFailure(AppConstants.OTHER_CASE, e.message!!)
                } catch (e: SocketTimeoutException) {
                    authListener?.onFailure(
                        AppConstants.OTHER_CASE,
                        "Something went wrong! Please try again later."
                    )
                }
            }
        }
    }

    fun onLogoutButtonClicked() {
        userRepository.deleteCredential(key)
    }
}

