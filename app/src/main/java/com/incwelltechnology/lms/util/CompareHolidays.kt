package com.incwelltechnology.lms.util

import com.incwelltechnology.lms.model.Holiday

class CompareHolidays {
    companion object : Comparator<Holiday> {
        override fun compare(a: Holiday, b: Holiday) = when {
            a.days != b.days -> a.days - b.days
            else -> a.days - b.days
        }
    }
}