package com.incwelltechnology.lms.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {
    lateinit var dataBinding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayout())
    }

    abstract fun getLayout(): Int
}