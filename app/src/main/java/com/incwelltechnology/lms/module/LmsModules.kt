package com.incwelltechnology.lms.module

import com.incwelltechnology.lms.data.network.LmsApi
import com.incwelltechnology.lms.data.network.LoginApi
import com.incwelltechnology.lms.data.network.NetworkConnectionInterceptor
import com.incwelltechnology.lms.data.network.UserTokenInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module

val lmsModule:Module= module {
    single { LoginApi(get()) }
    single { LmsApi(get(),get()) }
    single { UserTokenInterceptor() }
    single { NetworkConnectionInterceptor(get()) }
}