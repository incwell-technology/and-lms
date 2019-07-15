package com.incwelltechnology.lms.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivityLoginBinding
import com.incwelltechnology.lms.util.toast

class LoginActivity : AppCompatActivity(), AuthListener {
    val TAG = LoginActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.authListener = this
    }

    override fun onStarted() {
        //progress_bar.show()
    }

    override fun onSuccess(loginResponse: LiveData<String>) {
        loginResponse.observe(this, Observer {
            //progress_bar.hide()
            toast(it)
            Log.d(TAG, it)
        })
    }

    override fun onFailure(message: String) {
        //progress_bar.hide()
        toast(message)
    }
}
