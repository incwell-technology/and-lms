package com.incwelltechnology.lms.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.authenticationServices.AuthenticationService
import com.incwelltechnology.lms.authenticationServices.BaseResponse
import com.incwelltechnology.lms.authenticationServices.ServiceBuilder
import com.incwelltechnology.lms.model.Code
import kotlinx.android.synthetic.main.activity_verifycode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifycodeActivity : AppCompatActivity() {

    private val mService: AuthenticationService = ServiceBuilder
        .buildService(AuthenticationService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifycode)

        verifyBtn.setOnClickListener {
            val code :Int = pincode.text.toString().toInt()
            val verifiedCode = Code(code)
            mService.verifyCode(verifiedCode)
                .enqueue(object : Callback<BaseResponse<Code>> {
                    override fun onFailure(call: Call<BaseResponse<Code>>, t: Throwable) {
                        Log.d("testPasswordReset", "" + t)
                    }
                    override fun onResponse(
                        call: Call<BaseResponse<Code>>,
                        response: Response<BaseResponse<Code>>
                    ) {
                        if (response.body()!!.status) {
                            val intent= Intent(this@VerifycodeActivity, ChangepasswordActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left)
                        } else {
                            Toast.makeText(this@VerifycodeActivity, "Error", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.slide_out_right,
            R.anim.slide_out_left
        )
    }
}
