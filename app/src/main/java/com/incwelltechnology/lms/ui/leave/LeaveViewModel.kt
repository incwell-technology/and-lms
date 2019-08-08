package com.incwelltechnology.lms.ui.leave

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.LeaveRepository
import com.incwelltechnology.lms.util.Coroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            val leaveResponse = leaveRepository.applyLeave(type!!, startDate!!, endDate!!, halfDay!!, leaveReason!!)
            if (leaveResponse.body()!!.status) {
                withContext(Dispatchers.Main) {
                    message.value = "Leave Request sent successfully"
                }
            } else {
                withContext(Dispatchers.Main) {
                    message.value = "Sorry! Your request failed."
                }
            }
        }
    }
}