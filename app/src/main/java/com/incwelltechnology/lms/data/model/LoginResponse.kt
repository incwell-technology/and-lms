package com.incwelltechnology.lms.data.model

data class LoginResponse(
    var status: Boolean,
    var data: User,
    var error:String,
    var token:String
)