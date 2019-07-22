package com.incwelltechnology.lms

import android.app.Application
import com.incwelltechnology.lms.module.employeeModule
import com.incwelltechnology.lms.module.homeModule
import com.incwelltechnology.lms.module.lmsModule
import com.incwelltechnology.lms.module.networkModule
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(lmsModule,networkModule, homeModule, employeeModule))
        }
    }
}