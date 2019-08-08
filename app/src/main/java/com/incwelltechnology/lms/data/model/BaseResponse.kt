package com.incwelltechnology.lms.data.model

class BaseResponse<T> {
    var status: Boolean = false
    var data: T? = null
    var error: String = ""
}