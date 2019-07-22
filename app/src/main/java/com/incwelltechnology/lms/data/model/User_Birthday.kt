package com.incwelltechnology.lms.data.model

data class User_Birthday (
    val birthdays : List<Birthday>,
    val upcoming_bday : List<Upcoming_bday>,
    val upcoming_bday_visible : Boolean
)