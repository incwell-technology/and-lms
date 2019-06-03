package com.incwelltechnology.lms.model

data class LoginResponse(
    var status: Boolean,
    var data: UserProfile,
    var error:String
)