package com.incwelltechnology.lms.ui.reset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.repository.ResetRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.UndeclaredThrowableException
import java.net.ConnectException
import java.net.SocketTimeoutException

class ResetViewModel(private val resetRepository: ResetRepository) : ViewModel() {

    var verifiedEmailAddress: String? = null

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun onSubmitBtnClick() {
        Coroutine.io {
            try {
                val emailResponse = resetRepository.submitEmail(verifiedEmailAddress!!)
                Log.d("emailResponse", "$emailResponse")
                when {
                    emailResponse.body()?.status == true -> withContext(Dispatchers.Main) {
                        _message.value = "Check your email for password reset link!"
                    }
                    emailResponse.body()?.status == false -> withContext(Dispatchers.Main) {
                        _message.value = "Invalid Email Address!"
                    }
                    else -> withContext(Dispatchers.Main) {
                        _message.value = "Something went wrong!"
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _message.value = "No Internet Connection!"
                }

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    _message.value = "Something went wrong!"
                }
            } catch (e: UndeclaredThrowableException) {
                withContext(Dispatchers.Main) {
                    _message.value = "No Internet Connection!"
                }
            } catch (e: ConnectException) {
                withContext(Dispatchers.Main) {
                    _message.value = "Something went wrong!"
                }
            }
        }
    }
}
