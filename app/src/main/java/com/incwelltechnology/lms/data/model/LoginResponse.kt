package com.incwelltechnology.lms.data.model

data class LoginResponse(
    var status: Boolean,
    var data: Profile,
    var error:String,
    var token:String
)