package com.incwelltechnology.lms.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.User
import com.incwelltechnology.lms.databinding.ActivityLoginBinding
import com.incwelltechnology.lms.ui.home.DashboardActivity
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.hideErrorHint
import com.incwelltechnology.lms.util.show
import com.incwelltechnology.lms.util.snack
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), AuthListener {
    private val authViewModel:AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel=authViewModel
        authViewModel.authListener = this

        //eliminating error hint when editext is started to fill
        hideErrorHint(username,layout_username)
        hideErrorHint(password,layout_password)

        //translate animation for layout
        loginPageAnimate(this,800L,l1)
        loginPageAnimate(this,500L,l2)
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(user: User) {
        progress_bar.hide()
        val intent=Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left)
//        toast("$user")
    }

    override fun onFailure(code:Int,message: String) {
        progress_bar.hide()
        when (code) {
            0 -> {
                layout_username.error=message
                layout_username.requestFocus()
            }
            1 -> {
                layout_password.error=message
                layout_password.requestFocus()
            }
            else -> {
                login.snack(message)
//                toast(message)
            }
        }
    }

    //function to translate layout from down to up
    private fun loginPageAnimate(context: Context, duration:Long, view:View){
        val animation:Animation= AnimationUtils.loadAnimation(context, R.anim.downtoup)
        animation.duration=duration
        view.animation=animation
    }
}
