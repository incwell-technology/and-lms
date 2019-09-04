package com.incwelltechnology.lms.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.model.*
import com.incwelltechnology.lms.data.repository.DashboardRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
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

    var leaveId:Int ?= null

    //1. will be called from HomeFragment
    fun loadData() {
        Coroutine.io {
            try {
                val leaveRequest = dashboardRepository.getLeaveRequest()
                when {
                    leaveRequest.body()?.status == true -> withContext(Dispatchers.Main) {
                        _usrLeaveRequest.value = leaveRequest.body()!!.data
                    }
                    leaveRequest.body()?.status == false -> withContext(Dispatchers.Main) {
                        _usrLeaveRequest.value = leaveRequest.body()!!.data
                    }
                    else -> withContext(Dispatchers.Main) {
                        _errorResponse.value = "No registered User"
                    }
                }

                val userAtLeaveResponse = dashboardRepository.getUserAtLeave()
                if (userAtLeaveResponse.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        _usrLeaveResponse.value = userAtLeaveResponse.body()!!.data
                    }
                }
                val userBirthdayResponse = dashboardRepository.getBirthday()
                if (userBirthdayResponse.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        _birthdayResponse.value = userBirthdayResponse.body()!!.data?.birthdays
                    }
                }
                val publicHolidayResponse = dashboardRepository.getPublicHolidays()
                if (publicHolidayResponse.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        _holidayResponse.value = publicHolidayResponse.body()!!.data
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

    //2. will be called from ProfileFragment
    fun uploadProfile(imageData: MultipartBody.Part, userId: Int) {
        Coroutine.io {
            try {
                val myResponse = dashboardRepository.getProfilePictureChanged(imageData, userId)
                if (myResponse.body()!!.status) {
                    withContext(Dispatchers.Main) {
                        //hit api request to reload
                        _usrProImage.postValue(myResponse.body()?.data?.image)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d("img2", myResponse.body()!!.error)
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
                val responseForLeaveRequest = dashboardRepository.responseLeaveRequest(leaveId!!,"1")
                when {
                    responseForLeaveRequest.body()?.status == true -> {
                        withContext(Dispatchers.Main) {
                            _messageResponse.value="Approval Success"
                        }
                    }
                    responseForLeaveRequest.body()?.status == false -> {
                        withContext(Dispatchers.Main) {
                            _messageResponse.value="Error occurred!"
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
    }


    fun onRejectBtnClicked() {
        Coroutine.io {
            try {
                val responseForLeaveRequest = dashboardRepository.responseLeaveRequest(leaveId!!,"2")
                when {
                    responseForLeaveRequest.body()?.status == true -> {
                        withContext(Dispatchers.Main) {
                            _messageResponse.value="Rejection Successful"

                        }
                    }
                    responseForLeaveRequest.body()?.status == false -> {
                        withContext(Dispatchers.Main) {
                            _messageResponse.value="Error occurred!"
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
    }
}
