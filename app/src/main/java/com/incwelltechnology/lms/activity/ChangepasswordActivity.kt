package com.incwelltechnology.lms.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.incwelltechnology.lms.R
import kotlinx.android.synthetic.main.activity_changepassword.*

class ChangepasswordActivity : AppCompatActivity() {
    private lateinit var _newPassword: String
    private lateinit var _confirmPassword: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepassword)
        if (savedInstanceState != null) {
            val getNewPassword = savedInstanceState.getString("new_password")
            val getConfirmedPassword = savedInstanceState.getString("confirm_password")
            newPassword.setText(getNewPassword)
            confirmPassword.setText(getConfirmedPassword)
        }

        newPassword.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newPasswordLayout.isErrorEnabled=false
            }
        })

        confirmPassword.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                confirmPasswordLayout.isErrorEnabled=false
            }
        })

        changePassword.setOnClickListener {
            _newPassword=newPassword.text.toString()
            _confirmPassword=confirmPassword.text.toString()
            when {
                _newPassword.isEmpty() -> {
                    newPasswordLayout.error = "Field is empty"
                    newPasswordLayout.requestFocus()
                    return@setOnClickListener
                }
                _confirmPassword.isEmpty() -> {
                    confirmPasswordLayout.error = "Field is empty"
                    confirmPasswordLayout.requestFocus()
                    return@setOnClickListener
                }
                _newPassword != _confirmPassword -> {
                    Log.d("checkPass", "Password didnot match")
                    return@setOnClickListener
                }
                else -> Log.d("checkPass", "Proceed ahead")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("checkPass","onSaveInstanceState")
        _newPassword = newPassword.text.toString()
        _confirmPassword = confirmPassword.text.toString()
        outState.putString("new_password", _newPassword)
        outState.putString("confirm_password", _confirmPassword)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.slide_out_right,
            R.anim.slide_out_left
        )
    }
}
