package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.network.LmsApi
import com.incwelltechnology.lms.data.network.NetworkConnectionInterceptor
import com.incwelltechnology.lms.data.network.UserTokenInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module

val lmsModule:Module= module {
    single { LmsApi(get(),get()) }
    single { NetworkConnectionInterceptor(get()) }
    single { UserTokenInterceptor(get())}
}