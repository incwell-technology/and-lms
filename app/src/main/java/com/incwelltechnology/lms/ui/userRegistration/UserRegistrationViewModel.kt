package com.incwelltechnology.lms.ui.userRegistration

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.AdminRepository
import com.incwelltechnology.lms.util.Coroutine
import java.lang.Exception

class UserRegistrationViewModel(private val adminRepository: AdminRepository) : ViewModel() {
    var firstName: String? = null
    var lastName: String? = null
    var birthDate: String? = null
    var joinDate: String? = null
    var phoneNumber: String? = null
    var userDepartment: String? = null
    var userEmail: String? = null
    var userName: String? = null
    var passWord: String? = null

    private val _userCreationResponse = MutableLiveData<Boolean>()
    val userCreationRespone: LiveData<Boolean>
        get() = _userCreationResponse

    fun getUserForRegistration(
        firstname: String,
        lastname: String,
        username: String,
        password: String,
        email: String,
        department: String,
        phonenumber: String,
        birthdate: String,
        joindate: String
    ) {
        firstName = firstname
        lastName = lastname
        userName = username
        passWord = password
        userEmail = email
        userDepartment = department
        phoneNumber = phonenumber
        birthDate = birthdate
        joinDate = joindate
    }

    fun onRegisterBtnClicked() {
        Coroutine.io {
            try {
                val userRegistrationResponse = adminRepository.registerUser(
                    firstName!!,
                    lastName!!,
                    userName!!,
                    passWord!!,
                    userEmail!!,
                    userDepartment!!,
                    phoneNumber!!,
                    birthDate!!,
                    joinDate!!
                )
                when {
                    userRegistrationResponse.body()?.status == true -> {
                        _userCreationResponse.value = true
                    }
                    userRegistrationResponse.body()?.status == false -> {
                        _userCreationResponse.value = false
                    }
                    else -> {
                        _userCreationResponse.value = false
                    }
                }
            } catch (e: Exception) {

            }
        }
    }
}