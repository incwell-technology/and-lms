package com.incwelltechnology.lms.ui.auth

import com.incwelltechnology.lms.data.model.User

interface AuthListener {
    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(code:Int,message:String)
}