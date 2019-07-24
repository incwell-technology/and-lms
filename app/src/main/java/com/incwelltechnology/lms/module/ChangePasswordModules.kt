package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.repository.ChangePasswordRepository
import com.incwelltechnology.lms.ui.passwordRecovery.ChangePasswordActivity
import com.incwelltechnology.lms.ui.passwordRecovery.ChangePasswordViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val changePasswordModule= module {
    viewModel { ChangePasswordViewModel(get()) }
    single { ChangePasswordActivity() }
    single { ChangePasswordRepository(get()) }
}