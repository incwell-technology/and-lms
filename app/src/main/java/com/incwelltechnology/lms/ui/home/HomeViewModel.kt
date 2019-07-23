package com.incwelltechnology.lms.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.model.Birthday
import com.incwelltechnology.lms.data.model.Holiday
import com.incwelltechnology.lms.data.model.Leave
import com.incwelltechnology.lms.data.repository.DashboardRepository
import com.incwelltechnology.lms.util.Coroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {
    var usrLeaveResponse:MutableLiveData<List<Leave>> = MutableLiveData()
    var birthdayResponse: MutableLiveData<List<Birthday>> = MutableLiveData()
    var holidayResponse:MutableLiveData<List<Holiday>> = MutableLiveData()

    fun loadUserAtLeave(){
        Coroutine.io{
            val res= dashboardRepository.getUserAtLeave()
            if(res.body()!!.status){
                withContext(Dispatchers.Main) {
                    usrLeaveResponse.value=res.body()!!.data
                }
            }
        }
    }

    fun loadBirthday() {
        Coroutine.io {
            val res = dashboardRepository.getBirthday()
            if (res.body()?.status == true) {
                withContext(Dispatchers.Main) {
                    birthdayResponse.value = res.body()!!.data?.birthdays
                }
            }
        }
    }

    fun loadHoliday() {
        Coroutine.io {
            val res = dashboardRepository.getPublicHolidays()
            if (res.body()?.status == true) {
                withContext(Dispatchers.Main) {
                    holidayResponse.value = res.body()!!.data
                }
            }
        }
    }
}
