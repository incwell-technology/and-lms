package com.incwelltechnology.lms.model

data class LeaveCreate(
    val type:String,
    val from_date:String,
    val to_date:String,
    val half_day:Boolean,
    val leave_reason:String
)