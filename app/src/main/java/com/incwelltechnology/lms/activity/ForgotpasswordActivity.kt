package com.incwelltechnology.lms.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.authenticationServices.AuthenticationService
import com.incwelltechnology.lms.authenticationServices.BaseResponse
import com.incwelltechnology.lms.authenticationServices.ServiceBuilder
import com.incwelltechnology.lms.model.ResetPassword
import kotlinx.android.synthetic.main.activity_forgotpassword.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotpasswordActivity : AppCompatActivity() {

    private val mService: AuthenticationService = ServiceBuilder
        .buildService(AuthenticationService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)

        submit_email.setOnClickListener {
//            val intent=Intent(this@ForgotpasswordActivity, VerifycodeActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left)
            val email = verified_email.text.toString()
            val verifiedEmail = ResetPassword(email)

            mService.verifyEmail(verifiedEmail)
                .enqueue(object : Callback<BaseResponse<ResetPassword>> {
                    override fun onFailure(call: Call<BaseResponse<ResetPassword>>, t: Throwable) {
                        Log.d("testPasswordReset", "" + t)
                    }
                    override fun onResponse(
                        call: Call<BaseResponse<ResetPassword>>,
                        response: Response<BaseResponse<ResetPassword>>
                    ) {
                        if (response.body()!!.status) {
                            Toast.makeText(this@ForgotpasswordActivity, "Success", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@ForgotpasswordActivity, "Error", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })
        }
    }

}
