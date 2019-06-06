package com.incwelltechnology.lms.services

class BaseResponse<T> {
    var status: Boolean = false
    var data: T? = null
    var error: String = ""
}