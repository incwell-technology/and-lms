package com.incwelltechnology.lms.ui.leave

interface LeaveListener {
    fun onStarted()
    fun onSuccess(message: String)
    fun onFailure(message:String)
}