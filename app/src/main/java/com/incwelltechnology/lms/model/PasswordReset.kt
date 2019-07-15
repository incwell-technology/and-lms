package com.incwelltechnology.lms.model

data class PasswordReset(
    val new_password:String,
    val confirm_password:String
)