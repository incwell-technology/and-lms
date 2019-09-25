package com.incwelltechnology.lms.ui.home

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.incwelltechnology.lms.data.model.*
import com.incwelltechnology.lms.data.network.NetworkConnectionInterceptor
import com.incwelltechnology.lms.data.repository.DashboardRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
import com.incwelltechnology.lms.util.snack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.lang.reflect.UndeclaredThrowableException
import java.net.ConnectException
import java.net.SocketTimeoutException

class HomeViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {

    private val _usrLeaveRequest = MutableLiveData<List<RequestLeave>>()
    val usrLeaveRequest: LiveData<List<RequestLeave>>
        get() = _usrLeaveRequest

    private val _usrLeaveResponse = MutableLiveData<List<Leave>>()
    val usrLeaveResponse: LiveData<List<Leave>>
        get() = _usrLeaveResponse

    private val _birthdayResponse = MutableLiveData<List<Birthday>>()
    val birthdayResponse: LiveData<List<Birthday>>
        get() = _birthdayResponse

    private val _holidayResponse = MutableLiveData<List<Holiday>>()
    val holidayResponse: LiveData<List<Holiday>>
        get() = _holidayResponse

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String>
        get() = _errorResponse

    private val _usrProImage = MutableLiveData<String>()
    val usrProImage: LiveData<String>
        get() = _usrProImage

    private val _notificationList = MutableLiveData<List<Notifications>>()
    val notificationList: LiveData<List<Notifications>>
        get() = _notificationList

    private val _messageResponse = MutableLiveData<String>()
    val messageResponse: LiveData<String>
        get() = _messageResponse

    var leaveId: Int? = null

    //1. will be called from HomeFragment
    fun loadData() {
        Coroutine.io {
            try {
                when {
                    dashboardRepository.getLeaveRequest().body()?.status == true -> withContext(
                        Dispatchers.Main
                    ) {
                        _usrLeaveRequest.value = dashboardRepository.getLeaveRequest().body()!!.data
                    }
                    dashboardRepository.getLeaveRequest().body()?.status == false -> withContext(
                        Dispatchers.Main
                    ) {
                        _usrLeaveRequest.value = dashboardRepository.getLeaveRequest().body()!!.data
                    }
                }

                when {
                    dashboardRepository.getUserAtLeave().body()?.status == true -> withContext(
                        Dispatchers.Main
                    ) {
                        _usrLeaveResponse.value = dashboardRepository.getUserAtLeave().body()!!.data
                    }
                    dashboardRepository.getUserAtLeave().body()?.status == false -> withContext(
                        Dispatchers.Main
                    ) {

                    }
                    else -> withContext(Dispatchers.Main) {

                    }
                }

                when {
                    dashboardRepository.getBirthday().body()?.status == true -> withContext(
                        Dispatchers.Main
                    ) {
                        _birthdayResponse.value =
                            dashboardRepository.getBirthday().body()!!.data?.birthdays
                    }
                    dashboardRepository.getBirthday().body()?.status == false -> withContext(
                        Dispatchers.Main
                    ) {

                    }
                    else -> withContext(Dispatchers.Main) {

                    }
                }

                when {
                    dashboardRepository.getPublicHolidays().body()?.status == true -> withContext(
                        Dispatchers.Main
                    ) {
                        _holidayResponse.value =
                            dashboardRepository.getPublicHolidays().body()!!.data
                    }
                    dashboardRepository.getPublicHolidays().body()?.status == false -> withContext(
                        Dispatchers.Main
                    ) {

                    }
                    else -> withContext(Dispatchers.Main) {

                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _errorResponse.value = e.message
                }
            } catch (e: UndeclaredThrowableException) {
                withContext(Dispatchers.Main) {
                    _errorResponse.value = e.undeclaredThrowable.message
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    _errorResponse.value = e.message
                }
            }
        }
    }

    //2. will be called from ProfileFragment
    fun uploadProfile(imageData: MultipartBody.Part, userId: Int, view: View) {
        Coroutine.io {
            try {
                val myResponse = dashboardRepository.getProfilePictureChanged(imageData, userId)
                if (myResponse.body()!!.status) {
                    withContext(Dispatchers.Main) {
                        //hit api request to reload
                        _usrProImage.value = myResponse.body()?.data?.image
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        view.snack(myResponse.body()!!.error)
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    view.snack(e.message!!)
                }

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    view.snack("Something went wrong!")
                }
            } catch (e: UndeclaredThrowableException) {
                withContext(Dispatchers.Main) {
                    view.snack(e.undeclaredThrowable.message!!)
                }
            }
        }
    }

    //3. will be called from NotificationFragment
    fun loadNotificationList() {
        Coroutine.io {
            try {
                val notificationResponse = dashboardRepository.getNotificationList()
                if (notificationResponse.body()!!.status) {
                    withContext(Dispatchers.Main) {
                        _notificationList.value = notificationResponse.body()!!.data
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d("notification", notificationResponse.body()!!.error)
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _errorResponse.value = "No Internet Connection!"
                }

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    _errorResponse.value = "Something went wrong!"
                }
            } catch (e: UndeclaredThrowableException) {
                withContext(Dispatchers.Main) {
                    _errorResponse.value = "No Internet Connection!"
                }
            } catch (e: ConnectException) {
                withContext(Dispatchers.Main) {
                    _errorResponse.value = "Something went wrong!"
                }
            }
        }
    }

    fun onAcceptBtnClicked() {
        Coroutine.io {
            try {
                val responseForLeaveRequest =
                    dashboardRepository.responseLeaveRequest(leaveId!!, "1")
                when {
                    responseForLeaveRequest.body()?.status == true -> {
                        withContext(Dispatchers.Main) {
                            _messageResponse.value = "Approval Success"
                        }
                    }
                    responseForLeaveRequest.body()?.status == false -> {
                        withContext(Dispatchers.Main) {
                            _messageResponse.value = "Error occurred!"
                        }
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _messageResponse.value = e.message
                }
            }
        }
    }


    fun onRejectBtnClicked() {
        Coroutine.io {
            try {
                val responseForLeaveRequest =
                    dashboardRepository.responseLeaveRequest(leaveId!!, "1")
                when {
                    responseForLeaveRequest.body()?.status == true -> {
                        withContext(Dispatchers.Main) {
                            _messageResponse.value = "Approval Success"
                        }
                    }
                    responseForLeaveRequest.body()?.status == false -> {
                        withContext(Dispatchers.Main) {
                            _messageResponse.value = "Error occurred!"
                        }
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _messageResponse.value = e.message
                }
            }
        }
    }
}
