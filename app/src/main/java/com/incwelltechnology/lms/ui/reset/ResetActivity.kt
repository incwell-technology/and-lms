package com.incwelltechnology.lms.ui.reset

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivityResetBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.ui.auth.LoginActivity
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.hideErrorHint
import com.incwelltechnology.lms.util.show
import com.incwelltechnology.lms.util.snack
import kotlinx.android.synthetic.main.activity_reset.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetActivity : BaseActivity<ActivityResetBinding>(){
    private val resetViewModel:ResetViewModel by viewModel()
    override fun getLayout(): Int {
        return R.layout.activity_reset
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.dataBinding.resetViewmodel=resetViewModel

        verifyEmailBtn.setOnClickListener {
            when{
                resetViewModel.verifiedEmailAddress.isNullOrEmpty()->{
                    til_email.error="Email field is empty"
                    til_email.requestFocus()
                    return@setOnClickListener
                }
                else ->{
                    cpb.show()
                    resetViewModel.onSubmitBtnClick()
                    resetViewModel.message.observe(this, Observer {
                        cpb.hide()
                        verifyEmailBtn.snack(it)
                        verifiedEmail.text?.clear()
                    })
                }
            }
        }

        // Clear the error once key is typed
        verifiedEmail.setOnKeyListener { _, _, _ ->
            if (!resetViewModel.verifiedEmailAddress.isNullOrEmpty()) {
                // Clear the error.
                til_email.error = null
            }
            false
        }


        mtrl_back.setOnClickListener {
            val intent= Intent(this, LoginActivity::class.java)
            intent.flags= FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        hideErrorHint(verifiedEmail,til_email)
    }
}
