package com.incwelltechnology.lms.ui.employee

import com.incwelltechnology.lms.data.network.LmsApi

class EmployeeRepository(private val lmsApi: LmsApi) {
    suspend fun getEmployee()=lmsApi.getEmployee()
}