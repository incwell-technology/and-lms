package com.incwelltechnology.lms.ui.auth

import android.content.Intent
import android.os.Bundle
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.User
import com.incwelltechnology.lms.databinding.ActivityLoginBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.ui.home.DashboardActivity
import com.incwelltechnology.lms.ui.reset.ResetActivity
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.hideErrorHint
import com.incwelltechnology.lms.util.show
import com.incwelltechnology.lms.util.snack
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(), AuthListener {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.dataBinding.viewmodel = authViewModel

        authViewModel.authListener = this

        authViewModel.sharedPreference()

        if (authViewModel.isPresent!!) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
            finish()
        }

        //eliminating error hint when editext is started to fill
        hideErrorHint(username, til_username)
        hideErrorHint(password, til_password)

        //translate layout
//        translateUp(this, 500L, upperLayout)
//        translateUp(this, 800L, bottomLayout)

        mtrl_forgotPwdBtn.setOnClickListener {
            val intent=Intent(this, ResetActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }


    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(user: User) {
        progress_bar.hide()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
        finish()
    }

    override fun onFailure(code: Int, message: String) {
        progress_bar.hide()
        when (code) {
            AppConstants.USERNAME_EMPTY -> {
                til_username.error = message
                til_username.requestFocus()
            }
            AppConstants.PASSWORD_EMPTY -> {
                til_password.error = message
                til_password.requestFocus()
            }
            else -> {
                mtrl_loginBtn.snack(message)
            }
        }
    }

    //translate layout
//    private fun translateUp(context: Context, duration: Long, view: View) {
//        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.downtoup)
//        animation.duration = duration
//        view.animation = animation
//    }
}
