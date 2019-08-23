package com.incwelltechnology.lms.data.model

data class Registration(
    val first_name: String,
    val last_name: String,
    val username: String,
    val password: String,
    val email: String,
    val department: String,
    var phone_number: String,
    val date_of_birth: String,
    val joined_date: String
)