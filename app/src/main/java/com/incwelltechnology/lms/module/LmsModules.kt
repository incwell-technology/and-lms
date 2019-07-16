package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.network.LmsApi
import com.incwelltechnology.lms.data.repository.UserRepository
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val myModule:Module= module {
    viewModel { AuthViewModel(get()) }
    single { LmsApi() }
    single { UserRepository(get()) }
}