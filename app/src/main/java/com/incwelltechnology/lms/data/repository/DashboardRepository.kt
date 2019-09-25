package com.incwelltechnology.lms.data.repository

import com.incwelltechnology.lms.data.model.BaseResponse
import com.incwelltechnology.lms.data.model.LeaveRequestResponse
import com.incwelltechnology.lms.data.model.ProfileImage
import com.incwelltechnology.lms.data.network.LmsApi
import com.incwelltechnology.lms.firebase.FirebaseService
import okhttp3.MultipartBody
import retrofit2.Response

class DashboardRepository(private val lmsApi: LmsApi) {

    suspend fun getLeaveRequest() = lmsApi.getLeaveRequest()
    suspend fun getUserAtLeave() = lmsApi.getLeaveToday()
    suspend fun getBirthday() = lmsApi.getBirthday()
    suspend fun getPublicHolidays() = lmsApi.getHoliday()
    suspend fun getProfilePictureChanged(
        image: MultipartBody.Part,
        userId: Int
    ): Response<BaseResponse<ProfileImage>> {
        return lmsApi.changeProfilePicture(userId, image)
    }

    suspend fun getNotificationList() = lmsApi.getNotifications()

    suspend fun responseLeaveRequest(
        leaveId: Int,
        leave_response: String
    ): Response<BaseResponse<LeaveRequestResponse>> {
        val leaveResponse = LeaveRequestResponse(leave_response)
        return lmsApi.responseForLeaveRequest(leaveId, leaveResponse)
    }
}