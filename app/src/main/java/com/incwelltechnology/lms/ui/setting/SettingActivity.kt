package com.incwelltechnology.lms.ui.setting

import android.os.Bundle
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivitySettingBinding
import com.incwelltechnology.lms.ui.BaseActivity
import kotlinx.android.synthetic.main.custom_toolbar.*

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun getLayout(): Int {
        return R.layout.activity_setting
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //custom toolbar
        setSupportActionBar(custom_toolbar)
        custom_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        custom_toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}
