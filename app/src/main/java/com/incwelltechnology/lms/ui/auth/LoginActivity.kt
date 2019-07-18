package com.incwelltechnology.lms.ui.auth

import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.base.BaseActivity
import com.incwelltechnology.lms.databinding.ActivityLoginBinding


class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    val TAG = LoginActivity::class.java.simpleName
    override fun getLayout(): Int {
        return R.layout.activity_login
    }
}
