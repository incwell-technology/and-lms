package com.incwelltechnology.lms.ui.passwordRecovery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.ChangePasswordRepository
import com.incwelltechnology.lms.util.Coroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePasswordViewModel(private val changePasswordRepository: ChangePasswordRepository) :
    ViewModel() {

    var newPass: String? = null
    var confirmPass: String? = null
    var linkFromMail: String? = null

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int>
        get() = _userId

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String>
        get() = _msg

    private val _res = MutableLiveData<String>()
    val res: LiveData<String>
        get() = _res

    fun getLink(link: String) {
        linkFromMail = link
        Log.d("data2", linkFromMail!!)
    }

    fun passLink() {
        Coroutine.io {
            val check = changePasswordRepository.checkLink(linkFromMail!!)
            if (check.body()!!.status) {
                withContext(Dispatchers.Main) {
                    _userId.value = check.body()?.data!!.id
                    Log.d("data", "${check.body()?.data!!.id}")
                }
            } else {
                withContext(Dispatchers.Main) {
                    Log.d("data", check.body()!!.error)
                    _msg.value = check.body()!!.error
                }
            }
        }
    }

    fun onChangePassBtnClick() {
        Coroutine.io {
            val passwords =
                changePasswordRepository.changePassword(newPass!!, confirmPass!!, userId.value!!)
            if (passwords.body()!!.status) {
                withContext(Dispatchers.Main) {
                    Log.d("status1", "${passwords.body()?.status}")
                    _res.value = "Password Changed Successfully!"
                }
            } else {
                withContext(Dispatchers.Main) {
                    _res.value = "Error occurred while resetting your password!"
                }
            }
        }
    }

}