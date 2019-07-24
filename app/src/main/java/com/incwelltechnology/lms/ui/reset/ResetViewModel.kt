package com.incwelltechnology.lms.ui.reset

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.ResetRepository
import com.incwelltechnology.lms.util.Coroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResetViewModel(private val resetRepository: ResetRepository) :ViewModel() {

    var message:MutableLiveData<String> = MutableLiveData()
    var verifiedEmailAddress:String?=null

    fun onSubmitBtnClick(){
        Coroutine.io {
            val emailResponse=resetRepository.submitEmail(verifiedEmailAddress!!)
            if(emailResponse.body()!!.status){
                withContext(Dispatchers.Main){
                    message.value="Check your email for password reset link!"
                }
            }else{
                withContext(Dispatchers.Main){
                    message.value="Something went wrong!"
                }
            }
        }
    }
}