package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.network.LmsApi
import com.incwelltechnology.lms.data.network.NetworkConnectionInterceptor
import com.incwelltechnology.lms.data.repository.UserRepository
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val lmsModule: Module = module {
    viewModel { AuthViewModel(get()) }
    single { LmsApi(get()) }
    single { UserRepository(get()) }
    single { NetworkConnectionInterceptor(get()) }
}