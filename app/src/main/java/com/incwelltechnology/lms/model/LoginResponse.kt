package com.incwelltechnology.lms.model

data class LoginResponse(
    var status: Boolean,
    var data: Profile,
    var error:String,
    var token:String
)