package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.repository.UserRepository
import com.incwelltechnology.lms.data.storage.SharedPref
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: Module = module {
    viewModel { AuthViewModel(get()) }
    single { UserRepository(get()) }
    single { SharedPref }
}