package com.incwelltechnology.lms.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.authenticationServices.AuthenticationService
import com.incwelltechnology.lms.authenticationServices.BaseResponse
import com.incwelltechnology.lms.authenticationServices.ServiceBuilder
import com.incwelltechnology.lms.model.Compensation
import kotlinx.android.synthetic.main.activity_compensation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompensationActivity : AppCompatActivity() {
    private val mService: AuthenticationService = ServiceBuilder
        .buildService(AuthenticationService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compensation)

        //toolbar for an activity
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        apply_compensation.setOnClickListener {
            val days = number_of_days.text.toString()
            val leave_reason = compensation.text.toString()



            val applyCompensation= Compensation(days,leave_reason)

            mService.createCompensation(applyCompensation).enqueue(object : Callback<BaseResponse<Compensation>> {
                override fun onFailure(call: Call<BaseResponse<Compensation>>, t: Throwable) {

                }
                override fun onResponse(
                    call: Call<BaseResponse<Compensation>>,
                    response: Response<BaseResponse<Compensation>>
                ) {
                    if (response.body()!!.status) {
                        Toast.makeText(this@CompensationActivity,"Your request has been sent.", Toast.LENGTH_LONG).show()
                    } else {
                        Log.d("apply", "Problem")
                    }
                }
            })
        }
    }
}
