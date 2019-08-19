package com.incwelltechnology.lms.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Dateformat{
    //convert string of format "yyyy-MM-dd" to date of format "Jul 8, 2019"
    fun formatDate(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(formatter!!)
    }
}