package com.incwelltechnology.lms.ui.leave

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

    var leaveListener:LeaveListener ?= null

    fun getValues(
        type: String,
        from_date: String,
        to_date: String,
        leave: String,
        leave_reason: String
    ) {
        this.type = type
        startDate = from_date
        endDate = to_date
        halfDay = when (leave) {
            "Half Day" -> true
            "Full Day" -> false
            else -> return
        }
        leaveReason = leave_reason
    }

    fun onApplyButtonClick() {
        leaveListener!!.onStarted()
//        Log.d("test","$type,$startDate,$endDate,$halfDay,$leaveReason")
        Coroutine.io {
            val leaveResponse = leaveRepository.applyLeave(type!!, startDate!!, endDate!!, halfDay!!, leaveReason!!)
            if (leaveResponse.body()!!.status) {
                withContext(Dispatchers.Main) {
                    leaveListener!!.onSuccess("Leave Request sent successfully!")
                }
            } else {
                withContext(Dispatchers.Main) {
                    leaveListener!!.onFailure("Sorry! Your request failed.")
                }
            }
        }
    }
}