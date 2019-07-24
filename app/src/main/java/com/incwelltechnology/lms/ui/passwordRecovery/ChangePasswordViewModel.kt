package com.incwelltechnology.lms.ui.passwordRecovery

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.ChangePasswordRepository
import com.incwelltechnology.lms.util.Coroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePasswordViewModel(val changePasswordRepository: ChangePasswordRepository) : ViewModel() {

    val userId:MutableLiveData<Int> = MutableLiveData()
    val msg:MutableLiveData<String> = MutableLiveData()

    var newPass:String ?=null
    var confirmPass:String ?= null

    var linkFromMail: String? = null
    fun getLink(link: String) {
        linkFromMail = link
        Log.d("data2",linkFromMail)
    }

    fun passLink() {
        Coroutine.io{
            val check=changePasswordRepository.checkLink(linkFromMail!!)
            if(check.body()!!.status){
                withContext(Dispatchers.Main){
                    userId.value=check.body()?.data!!.id
                    Log.d("data","${check.body()?.data!!.id}")
                }
            }else{
                withContext(Dispatchers.Main){
                    Log.d("data", check.body()!!.error)
                    msg.value=check.body()!!.error
                }
            }
        }
    }

    fun onChangePassBtnClick(){
        Coroutine.io{
            val passwords=changePasswordRepository.changePassword(newPass!!,confirmPass!!,userId.value!!)
            if(passwords.body()!!.status){
                withContext(Dispatchers.Main){
                    msg.value="Password Changed Successfully!"
                }
            }
        }
    }

}