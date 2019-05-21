package com.incwelltechnology.lms.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.incwelltechnology.lms.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        loginBtn.setOnClickListener {
            val user=userName.text
            val password=userPassword.text
            Toast.makeText(this@MainActivity, user, Toast.LENGTH_LONG).show()
            Toast.makeText(this@MainActivity, password, Toast.LENGTH_LONG).show()
        }
    }





}
