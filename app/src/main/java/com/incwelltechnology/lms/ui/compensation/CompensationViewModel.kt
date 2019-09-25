package com.incwelltechnology.lms.ui.compensation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.CompensationRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompensationViewModel(private val compensationRepository: CompensationRepository) :
    ViewModel() {

    var days: String? = null
    var compensationReason: String? = null
    var message: MutableLiveData<String> = MutableLiveData()

    fun onApplyCompensationButtonClick() {
        Coroutine.io {
            try {
                val compensationResponse =
                    compensationRepository.createCompensation(days!!, compensationReason!!)
                if (compensationResponse.body()!!.status) {
                    withContext(Dispatchers.Main) {
                        message.value = "Request for compensation applied successfully!"
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        message.value = "Sorry! Your request failed."
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    message.value = e.message
                }
            }
        }
    }
}