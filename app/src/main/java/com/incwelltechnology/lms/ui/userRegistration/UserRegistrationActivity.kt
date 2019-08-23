package com.incwelltechnology.lms.ui.userRegistration

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivityUserRegistrationBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.ui.employee.EmployeeActivity
import com.incwelltechnology.lms.ui.home.fragment.DatePickerFragment
import com.incwelltechnology.lms.util.dropDown
import com.incwelltechnology.lms.util.hideErrorHint
import com.incwelltechnology.lms.util.hideErrorHintAutoCompleteTextView
import com.incwelltechnology.lms.util.snack
import kotlinx.android.synthetic.main.activity_user_registration.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.koin.android.ext.android.inject
import java.util.*

class UserRegistrationActivity : BaseActivity<ActivityUserRegistrationBinding>(),
    DatePickerDialog.OnDateSetListener {

    var flag: Int = 1

    private val userRegistrationViewModel: UserRegistrationViewModel by inject()

    override fun getLayout(): Int {
        return R.layout.activity_user_registration
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //must be for databinding
        super.dataBinding.userRegistration = userRegistrationViewModel
        //custom toolbar
        setSupportActionBar(custom_toolbar)
        custom_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        custom_toolbar.setNavigationOnClickListener {
            finish()
        }
        dropDown(this, AppConstants.departmentType, usrdepartment)
        birthdate.setOnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "birth date")
            flag = 0
        }
        joindate.setOnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "joined date")
            flag = 1
        }
        bindData()
        hideError()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val currentDate = "$year-${month + 1}-$dayOfMonth"
        if (flag == 0) {
            birthdate.text = Editable.Factory.getInstance().newEditable(currentDate)
        } else {
            joindate.text = Editable.Factory.getInstance().newEditable(currentDate)
        }
    }

    private fun bindData() {
        fab_register_user.setOnClickListener {
            when {
                firstname.text!!.isEmpty() -> {
                    til_firstname.error = getString(R.string.empty_field)
                    til_firstname.requestFocus()
                    return@setOnClickListener
                }
                lastname.text!!.isEmpty() -> {
                    til_lastname.error = getString(R.string.empty_field)
                    til_lastname.requestFocus()
                    return@setOnClickListener
                }
                birthdate.text!!.isEmpty() -> {
                    til_birthdate.error = getString(R.string.empty_field)
                    til_birthdate.requestFocus()
                    return@setOnClickListener
                }
                joindate.text!!.isEmpty() -> {
                    til_joindate.error = getString(R.string.empty_field)
                    til_joindate.requestFocus()
                    return@setOnClickListener
                }
                usrphone.text!!.isEmpty() -> {
                    til_phonenumber.error = getString(R.string.empty_field)
                    til_phonenumber.requestFocus()
                    return@setOnClickListener
                }
                usrdepartment.text.isEmpty() -> {
                    til_phonenumber.error = getString(R.string.empty_field)
                    til_phonenumber.requestFocus()
                    return@setOnClickListener
                }
                usremail.text!!.isEmpty() -> {
                    til_usremail.error = getString(R.string.empty_field)
                    til_usremail.requestFocus()
                    return@setOnClickListener
                }
                usrName.text!!.isEmpty() -> {
                    til_username.error = getString(R.string.empty_field)
                    til_username.requestFocus()
                    return@setOnClickListener
                }
                usrPassword.text!!.isEmpty() -> {
                    til_userpassword.error = getString(R.string.empty_field)
                    til_userpassword.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    userRegistrationViewModel.getUserForRegistration(
                        firstname.text.toString(),
                        lastname.text.toString(),
                        usrName.text.toString(),
                        usrPassword.text.toString(),
                        usremail.text.toString(),
                        usrdepartment.text.toString(),
                        usrphone.text.toString(),
                        birthdate.text.toString(),
                        joindate.text.toString()
                    )
                    userRegistrationViewModel.onRegisterBtnClicked()
                    userRegistrationViewModel.userCreationRespone.observe(this, Observer {
                        if (it) {
                            val intent = Intent(this, EmployeeActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
                        } else {
                            fab_register_user.snack("User registration failed!")
                        }
                    })
                }
            }
        }
    }

    private fun hideError() {
        hideErrorHint(firstname, til_firstname)
        hideErrorHint(lastname, til_lastname)
        hideErrorHint(birthdate, til_birthdate)
        hideErrorHint(joindate, til_joindate)
        hideErrorHint(usrphone, til_phonenumber)
        hideErrorHintAutoCompleteTextView(usrdepartment, til_department)
        hideErrorHint(usremail,til_usremail)
        hideErrorHint(usrName, til_username)
        hideErrorHint(usrPassword, til_userpassword)
    }
}
