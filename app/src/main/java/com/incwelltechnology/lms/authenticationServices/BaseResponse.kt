package com.incwelltechnology.lms.authenticationServices

class BaseResponse<T> {
    var status: Boolean = false
    var data: T? = null
    var error: String = ""
}