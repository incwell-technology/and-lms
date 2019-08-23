package com.incwelltechnology.lms.ui.noticeCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.AdminRepository
import com.incwelltechnology.lms.util.Coroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class NoticeViewModel(private val adminRepository: AdminRepository) : ViewModel() {
    var topic: String ?= null
    var notice: String ?= null

    private val _noticeResponse = MutableLiveData<String>()
    val noticeResponse: LiveData<String>
        get() = _noticeResponse

    fun getNotice(title:String,body:String){
        topic=title
        notice=body
    }
    fun postNotice() {
        try {
            Coroutine.io {
                val adminNotice = adminRepository.createNotice(topic!!, notice!!)
                if(adminNotice.body()!!.status){
                    withContext(Dispatchers.Main){
                        _noticeResponse.value = "Notice posted Successfully!"
                    }
                }else{
                    withContext(Dispatchers.Main){
                        _noticeResponse.value = "Problem occurred while posting notice!"
                    }
                }
            }
        }catch (e:Exception){
            _noticeResponse.value = "Something went Wrong!"
        }
    }
}