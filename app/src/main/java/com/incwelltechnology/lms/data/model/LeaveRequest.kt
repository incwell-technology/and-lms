package com.incwelltechnology.lms.data.model

data class LeaveRequest(
    val type:String,
    val from_date:String,
    val to_date:String,
    val half_day:Boolean,
    val leave_reason:String
)