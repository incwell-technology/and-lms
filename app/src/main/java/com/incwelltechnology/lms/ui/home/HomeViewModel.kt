package com.incwelltechnology.lms.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.model.Birthday
import com.incwelltechnology.lms.data.model.Holiday
import com.incwelltechnology.lms.data.model.Leave
import com.incwelltechnology.lms.data.repository.DashboardRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.lang.reflect.UndeclaredThrowableException
import java.net.SocketTimeoutException

class HomeViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {
    var usrLeaveResponse: MutableLiveData<List<Leave>> = MutableLiveData()
    var birthdayResponse: MutableLiveData<List<Birthday>> = MutableLiveData()
    var holidayResponse: MutableLiveData<List<Holiday>> = MutableLiveData()
    var errorResponse: MutableLiveData<String> = MutableLiveData()

    private val _usrProImage = MutableLiveData<String>()
    val usrProImage: LiveData<String>
        get() = _usrProImage

    //1. will be called from HomeFragment
    fun loadData() {
        Coroutine.io {
            try {
                val userAtLeaveResponse = dashboardRepository.getUserAtLeave()
                if (userAtLeaveResponse.body()!!.status) {
                    withContext(Dispatchers.Main) {
                        usrLeaveResponse.value = userAtLeaveResponse.body()!!.data
                    }
                }
                val userBirthdayResponse = dashboardRepository.getBirthday()
                if (userBirthdayResponse.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        birthdayResponse.value = userBirthdayResponse.body()!!.data?.birthdays
                    }
                }
                val publicHolidayResponse = dashboardRepository.getPublicHolidays()
                if (publicHolidayResponse.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        holidayResponse.value = publicHolidayResponse.body()!!.data
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    errorResponse.value = "No Internet Connection!"
                }

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    errorResponse.value = "Something went wrong!"
                }
            } catch (e: UndeclaredThrowableException) {
                withContext(Dispatchers.Main) {
                    errorResponse.value = "No Internet Connection!"
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
//                        _usrProImage.value=myResponse.body()?.data?.image
                        _usrProImage.postValue(myResponse.body()?.data?.image)

                        Log.d("img1", myResponse.body()!!.data!!.image)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d("img2", myResponse.body()!!.error)
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    errorResponse.value = "No Internet Connection!"
                }

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    errorResponse.value = "Something went wrong!"
                }
            } catch (e: UndeclaredThrowableException) {
                withContext(Dispatchers.Main) {
                    errorResponse.value = "No Internet Connection!"
                }
            }
        }
    }
}
