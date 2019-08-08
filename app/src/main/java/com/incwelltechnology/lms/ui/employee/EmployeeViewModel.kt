package com.incwelltechnology.lms.ui.employee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.model.Employee
import com.incwelltechnology.lms.data.repository.EmployeeRepository
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.UndeclaredThrowableException
import java.net.SocketTimeoutException

class EmployeeViewModel(private val employeeRepository: EmployeeRepository):ViewModel() {
    var employeeResponse: MutableLiveData<List<Employee>> = MutableLiveData()
    val errorResponse:MutableLiveData<String> = MutableLiveData()

    fun loadEmployee(){
        Coroutine.io {
            try {
                val res = employeeRepository.getEmployee()
                if (res.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        employeeResponse.value=res.body()?.data
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main){
                    errorResponse.value= "No Internet Connection!"
                }

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main){
                    errorResponse.value="Something went wrong!"
                }
            }catch (e: UndeclaredThrowableException){
                withContext(Dispatchers.Main){
                    errorResponse.value= "No Internet Connection!"
                }
            }
        }
    }
}