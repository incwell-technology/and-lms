package com.incwelltechnology.lms.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.incwelltechnology.lms.R

class VerifycodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifycode)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.slide_out_right,
            R.anim.slide_out_left
        )
    }
}
