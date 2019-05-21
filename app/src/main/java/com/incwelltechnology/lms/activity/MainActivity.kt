package com.incwelltechnology.lms.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.User
import com.incwelltechnology.lms.services.AuthenticationService
import com.incwelltechnology.lms.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        loginBtn.setOnClickListener {
            val user = userName.text
            val password = userPassword.text
            Toast.makeText(this@MainActivity, user, Toast.LENGTH_LONG).show()
            Toast.makeText(this@MainActivity, password, Toast.LENGTH_LONG).show()
        }

        var user = User("rajesh.k.khadka@gmail.com", "incwelltester")

        var service = ServiceBuilder.buildService(AuthenticationService::class.java)
        service.getUserList(user).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("MainActivity", response.body().toString())
            }

        })

    }


}
