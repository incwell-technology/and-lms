package com.incwelltechnology.lms.ui.passwordRecovery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivityChangePasswordBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.ui.auth.LoginActivity
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.show
import com.incwelltechnology.lms.util.snack
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {
    private val changePasswordViewModel: ChangePasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.dataBinding.link = changePasswordViewModel

        //uri for password recovery
        val data: Uri? = intent?.data
        val passLink = data.toString()

        val path: String = splitLink(passLink, AppConstants.SUBSTRING)

        //passing path to function getLink in viewmodel
        changePasswordViewModel.getLink(path)

        //calling function that passes link to repo
        changePasswordViewModel.passLink()

        val dataFromUrl: LiveData<Int> = changePasswordViewModel.userId
        dataFromUrl.observe(this, Observer {
            Log.d("data1", "${dataFromUrl.value}")
//            val userIdObtained=dataFromUrl.value
            changePasswordBtn.setOnClickListener {
                when {
                    changePasswordViewModel.newPass.isNullOrEmpty() -> {

                    }
                    changePasswordViewModel.confirmPass.isNullOrEmpty() -> {

                    }
                    changePasswordViewModel.newPass != changePasswordViewModel.confirmPass -> {

                    }
                    else -> {
                        p.show()
                        changePasswordViewModel.onChangePassBtnClick()
                        val status: LiveData<String> = changePasswordViewModel.msg
                        status.observe(this, Observer {
                            p.hide()
                            changePasswordBtn.snack(status.value!!)
                            Coroutine.io {
                                delay(2000L)
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }
                        })
                        this.finish()
                    }
                }
            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.activity_change_password
    }

    //extract path from url
    private fun splitLink(s1: String, s2: String): String {
        val str2 = Regex(s2)
        val result = str2.containsMatchIn(s1)
        return if (result) {
            s1.replace(str2, "")
        } else {
            ""
        }
    }
}
