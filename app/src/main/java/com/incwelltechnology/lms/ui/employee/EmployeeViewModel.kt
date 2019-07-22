package com.incwelltechnology.lms.ui.employee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.incwelltechnology.lms.data.model.Employee
import com.incwelltechnology.lms.util.Coroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmployeeViewModel(private val employeeRepository: EmployeeRepository):ViewModel() {
    var employeeResponse: MutableLiveData<List<Employee>> = MutableLiveData()

    fun loadEmployee(){
        Coroutine.io {
            val res = employeeRepository.getEmployee()
            if (res.body()?.status == true) {
                withContext(Dispatchers.Main) {
                   employeeResponse.value=res.body()?.data
                }
            }
        }
    }
}