package com.incwelltechnology.lms.data.model

data class RequestLeave(
    val id:Int,
    val full_name: String,
    val department:String,
    val from_date:String,
    val to_date:String,
    val total_days:Double,
    val leave_type:String,
    val leave_reason:String,
    var half_day:Boolean,
    var notification:Boolean
)