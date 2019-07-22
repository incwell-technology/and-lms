package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.network.LmsApi

class DashboardRepository(private val lmsApi: LmsApi){

   suspend fun getBirthday()=lmsApi.getBirthday()
   suspend fun getPublicHolidays()=lmsApi.getHoliday()

}