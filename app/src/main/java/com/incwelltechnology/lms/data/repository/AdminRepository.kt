package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.Notice
import com.incwelltechnology.lms.data.model.Registration
import com.incwelltechnology.lms.data.network.LmsApi
import retrofit2.Response

class AdminRepository(private val lmsApi: LmsApi) {
    suspend fun createNotice(title: String, notice: String): Response<BaseResponse<Notice>> {
        val mNotice = Notice(title, notice)
        return lmsApi.postNotice(mNotice)
    }

    suspend fun registerUser(
        first_name: String,
        last_name: String,
        username: String,
        password: String,
        email: String,
        department: String,
        phone_number: String,
        date_of_birth: String,
        joined_date: String
    ): Response<BaseResponse<Registration>> {
        val mRegistration = Registration(
            first_name,
            last_name,
            username,
            password,
            email,
            department,
            phone_number,
            date_of_birth,
            joined_date
        )
        return lmsApi.createUser(mRegistration)
    }
}