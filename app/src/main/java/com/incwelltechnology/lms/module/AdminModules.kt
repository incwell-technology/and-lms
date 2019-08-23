package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.repository.AdminRepository
import com.incwelltechnology.lms.ui.noticeCreation.CreateNoticeActivity
import com.incwelltechnology.lms.ui.noticeCreation.NoticeViewModel
import com.incwelltechnology.lms.ui.userRegistration.UserRegistrationActivity
import com.incwelltechnology.lms.ui.userRegistration.UserRegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val adminModule= module {
    viewModel { NoticeViewModel(get()) }
    viewModel { UserRegistrationViewModel(get()) }
    single { CreateNoticeActivity() }
    single { UserRegistrationActivity() }
    single { AdminRepository(get()) }
    
}