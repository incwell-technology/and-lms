package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.Compensation
import com.incwelltechnology.lms.data.network.LmsApi
import retrofit2.Response

class CompensationRepository(val lmsApi: LmsApi) {
    suspend fun createCompensation(days:String, leave_reason:String): Response<BaseResponse<Compensation>> {
        val compensation= Compensation(days,leave_reason)
        return lmsApi.createCompensation(compensation)
    }
}