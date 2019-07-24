package com.incwelltechnology.lms.ui.reset

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivityResetBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.util.hide
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
                    val mail:LiveData<String> =  resetViewModel.message
                    mail.observe(this, Observer {
                        cpb.hide()
                        verifyEmailBtn.snack("${mail.value}")
                        verifiedEmail.text?.clear()
                    })
                }
            }
        }
    }
}
