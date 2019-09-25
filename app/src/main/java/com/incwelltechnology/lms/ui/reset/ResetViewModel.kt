package com.incwelltechnology.lms.ui.reset

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
                when {
                    emailResponse.body()?.status == true -> withContext(Dispatchers.Main) {
                        _message.value = "Check your email for password reset link!"
                    }
                    emailResponse.body()?.status == false -> withContext(Dispatchers.Main) {
                        _message.value = "Invalid Email Address!"
                    }
                    else -> withContext(Dispatchers.Main) {
                        _message.value = emailResponse.message()
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _message.value = e.message
                }

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    _message.value = e.message
                }
            } catch (e: UndeclaredThrowableException) {
                withContext(Dispatchers.Main) {
                    _message.value = e.undeclaredThrowable.message
                }
            } catch (e: ConnectException) {
                withContext(Dispatchers.Main) {
                    _message.value = e.message
                }
            }
        }
    }
}
