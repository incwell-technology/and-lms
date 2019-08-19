package com.incwelltechnology.lms.data.model

data class User(
    val full_name : String = "",
    val phone_number: String = "",
    var email: String= "",
    val department: String = "",
    var leave_issuer: String ="",
    var image: String = "",
    var sick_leaves: Double,
    var annual_leaves:Double,
    var compensation_leaves: Double,
    val date_of_birth:String = "",
    val joined_date:String = "",
    var id:Int=0,
    var token:String=""
)