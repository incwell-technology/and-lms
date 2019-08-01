package com.incwelltechnology.lms.ui.home

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
import java.net.SocketTimeoutException

class HomeViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {
    var usrLeaveResponse: MutableLiveData<List<Leave>> = MutableLiveData()
    var birthdayResponse: MutableLiveData<List<Birthday>> = MutableLiveData()
    var holidayResponse: MutableLiveData<List<Holiday>> = MutableLiveData()

    fun loadUserAtLeave() {
        Coroutine.io {
            try {
                val res = dashboardRepository.getUserAtLeave()
                if (res.body()!!.status) {
                    withContext(Dispatchers.Main) {
                        usrLeaveResponse.value = res.body()!!.data
                    }
                }
            } catch (e: NoInternetException) {

            } catch (e: SocketTimeoutException) {

            }
        }
    }

    fun loadBirthday() {
        Coroutine.io {
            try {
                val res = dashboardRepository.getBirthday()
                if (res.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        birthdayResponse.value = res.body()!!.data?.birthdays
                    }
                }
            } catch (e: NoInternetException) {

            } catch (e: SocketTimeoutException) {

            }

        }
    }

    fun loadHoliday() {
        Coroutine.io {
            try {
                val res = dashboardRepository.getPublicHolidays()
                if (res.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        holidayResponse.value = res.body()!!.data
                    }
                }
            } catch (e: NoInternetException) {

            } catch (e: SocketTimeoutException) {

            }
        }
    }

}
