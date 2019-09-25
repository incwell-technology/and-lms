package com.incwelltechnology.lms.ui.compensation

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivityCompensationBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.hideErrorHint
import com.incwelltechnology.lms.util.show
import com.incwelltechnology.lms.util.snack
import kotlinx.android.synthetic.main.activity_compensation.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CompensationActivity : BaseActivity<ActivityCompensationBinding>() {

    private val compensationViewModel: CompensationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.dataBinding.cvm = compensationViewModel

        //custom toolbar
        setSupportActionBar(custom_toolbar)
        custom_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        custom_toolbar.setNavigationOnClickListener {
            finish()
        }

        hideErrorHint(days, til_days)
        hideErrorHint(compReason, til_reason)

        apply_compensation.setOnClickListener {
            when {
                compensationViewModel.days.isNullOrEmpty() -> {
                    til_days.error = "Days field cannot be Empty"
                    til_days.requestFocus()
                    return@setOnClickListener
                }
                compensationViewModel.compensationReason.isNullOrEmpty() -> {
                    til_reason.error = "Reason field cannot be Empty"
                    til_reason.requestFocus()
                    return@setOnClickListener
                }
                else -> {


                    compensationViewModel.onApplyCompensationButtonClick()
                    circular_progress.show()
                    val message: LiveData<String> = compensationViewModel.message
                    message.observe(this, Observer {
                        circular_progress.hide()
                        apply_compensation.snack("${message.value}")
                        days.text?.clear()
                        compReason.text?.clear()
                    })
                }
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_compensation
    }

}
