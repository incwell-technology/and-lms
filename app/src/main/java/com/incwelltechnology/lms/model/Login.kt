package com.incwelltechnology.lms.model

//This class is used to map the json keys to the object
data class Login(var username: String, var password:String, var fcm_token:String)