package com.incwelltechnology.lms.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.incwelltechnology.lms.R

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {
    lateinit var dataBinding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayout())
    }

    abstract fun getLayout(): Int
}