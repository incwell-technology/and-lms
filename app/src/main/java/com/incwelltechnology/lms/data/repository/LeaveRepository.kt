package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.LeaveRequest
import com.incwelltechnology.lms.data.network.LmsApi
import retrofit2.Response

class LeaveRepository(private val lmsApi: LmsApi){
    suspend fun applyLeave(
        type:String,
        from_date:String,
        to_date:String,
        half_day:Boolean,
        leave_reason:String
    ):Response<BaseResponse<LeaveRequest>>{
        val leave=LeaveRequest(type,from_date,to_date,half_day,leave_reason)
        return lmsApi.applyLeave(leave)
    }
}