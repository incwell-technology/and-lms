package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.ui.employee.EmployeeRepository
import com.incwelltechnology.lms.ui.employee.EmployeeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val employeeModule= module {
    viewModel { EmployeeViewModel(get()) }
    single { EmployeeRepository(get()) }
}