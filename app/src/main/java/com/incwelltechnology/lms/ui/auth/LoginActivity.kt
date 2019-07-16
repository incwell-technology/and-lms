package com.incwelltechnology.lms.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.User
import com.incwelltechnology.lms.databinding.ActivityLoginBinding
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.show
import com.incwelltechnology.lms.util.toast
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), AuthListener {
    val TAG = LoginActivity::class.java.simpleName
    private val authViewModel:AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel=authViewModel
        authViewModel.authListener = this
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(user: User) {
        progress_bar.hide()
        toast("$user")
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        toast(message)
    }
}
