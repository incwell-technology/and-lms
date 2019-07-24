package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.repository.ResetRepository
import com.incwelltechnology.lms.ui.reset.ResetActivity
import com.incwelltechnology.lms.ui.reset.ResetViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val passwordResetModule= module {
    viewModel { ResetViewModel(get()) }
    single { ResetRepository(get()) }
    single { ResetActivity() }
}