package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.repository.CompensationRepository
import com.incwelltechnology.lms.ui.compensation.CompensationActivity
import com.incwelltechnology.lms.ui.compensation.CompensationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val compensationModule= module {
    viewModel { CompensationViewModel(get()) }
    single { CompensationRepository(get()) }
    single { CompensationActivity() }
}