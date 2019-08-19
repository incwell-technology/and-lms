package com.incwelltechnology.lms.ui.leave

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.LeaveRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.UndeclaredThrowableException
import java.net.ConnectException
import java.net.SocketTimeoutException

class LeaveViewModel(private val leaveRepository: LeaveRepository) : ViewModel() {
    private var type: String? = null
    private var startDate: String? = null
    private var endDate: String? = null
    private var halfDay: Boolean? = null
    private var leaveReason: String? = null

    var message: MutableLiveData<String> = MutableLiveData()

    fun getValues(type: String, from_date: String, to_date: String, leaveType: Boolean, leave_reason: String){
        this.type = type
        startDate = from_date
        endDate = to_date
        halfDay = leaveType
        leaveReason = leave_reason
    }

    fun onApplyButtonClick() {
        Coroutine.io {
            try {
                val leaveResponse = leaveRepository.applyLeave(type!!, startDate!!, endDate!!, halfDay!!, leaveReason!!)
                Log.d("leaveResponse","$leaveResponse")
                when {
                    leaveResponse.body()?.status==true -> withContext(Dispatchers.Main) {
                        message.value = "Leave Request sent successfully"
                    }
                    leaveResponse.body()?.status==false -> withContext(Dispatchers.Main) {
                        message.value = "Something went wrong"
                    }
                    //when other exceptions are recieved
                    else -> withContext(Dispatchers.Main){
                        message.value="xxx"
                    }
                }
            }catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    message.value = "No Internet Connection!"
                }

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    message.value = "Something went wrong!"
                }
            } catch (e: UndeclaredThrowableException) {
                withContext(Dispatchers.Main) {
                    message.value = "No Internet Connection!"
                }
            }catch (e: ConnectException){
                withContext(Dispatchers.Main) {
                    message.value = "Something went wrong!"
                }
            }
        }
    }
}