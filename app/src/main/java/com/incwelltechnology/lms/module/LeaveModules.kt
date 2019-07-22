package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.repository.LeaveRepository
import com.incwelltechnology.lms.ui.leave.LeaveActivity
import com.incwelltechnology.lms.ui.leave.LeaveViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val leaveModule= module {
    viewModel { LeaveViewModel(get()) }
    single { LeaveActivity() }
    single { LeaveRepository(get()) }
}