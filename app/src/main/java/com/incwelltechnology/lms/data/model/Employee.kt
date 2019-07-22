package com.incwelltechnology.lms.data.model

data class Employee(
    val full_name: String,
    val email: String,
    var phone: String,
    var department: String,
    var leave_issuer: String,
    var image: String,
    var generate_report_access: Boolean
)