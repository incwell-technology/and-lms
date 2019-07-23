package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.repository.DashboardRepository
import com.incwelltechnology.lms.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val homeModule:Module= module {
    viewModel { HomeViewModel(get()) }
    single { DashboardRepository(get()) }
}