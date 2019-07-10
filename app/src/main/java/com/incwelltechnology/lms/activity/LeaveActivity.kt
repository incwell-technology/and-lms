package com.incwelltechnology.lms.activity

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.authenticationServices.AuthenticationService
import com.incwelltechnology.lms.authenticationServices.BaseResponse
import com.incwelltechnology.lms.authenticationServices.ServiceBuilder
import com.incwelltechnology.lms.model.LeaveCreate
import com.incwelltechnology.lms.view.fragment.DatePickerFragment
import kotlinx.android.synthetic.main.activity_leave.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LeaveActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    var tagNumber:Int=1
    private val mService: AuthenticationService = ServiceBuilder
        .buildService(AuthenticationService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave)

        val holiday = arrayOf("Half Day", "Full Day")
        val type = arrayOf("Annual Leave", "Sick Leave", "CompensationActivity Leave", "Unpaid Leave")
        dropDown(this, holiday, leave_dropdown)
        dropDown(this, type, type_of_leave_dropdown)

        //toolbar for an activity
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        start_date.setOnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "start date picker")
            tagNumber=0
        }
        end_date.setOnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "end date picker")
            tagNumber=1
        }

        apply_leave.setOnClickListener {
            val type = type_of_leave_dropdown.text.toString()
            val from_date = start_date.text.toString()
            val to_date = end_date.text.toString()
            val leave = leave_dropdown.text.toString()
            val leave_reason = reason.text.toString()
            val half_day:Boolean

            half_day = when (leave) {
                "Half Day" -> true
                "Full Day" -> false
                else -> return@setOnClickListener
            }
            when {
                from_date.isEmpty() -> {
                    layout_start_date.error = "Start Date Field is Empty"
                    layout_start_date.requestFocus()
                    return@setOnClickListener
                }
                to_date.isEmpty() -> {
                    layout_end_date.error = "End Date Field is Empty"
                    layout_end_date.requestFocus()
                    return@setOnClickListener
                }
                leave.isEmpty() -> {
                    layout_duration.error="Duration Field is Empty"
                    layout_duration.requestFocus()
                    return@setOnClickListener
                }
                type.isEmpty() -> {
                    layout_type_of_leave.error="Leave Type Field is Empty"
                    layout_type_of_leave.requestFocus()
                    return@setOnClickListener
                }
                leave_reason.isEmpty() -> {
                    layout_leave_reason.error="Leave Reason Field is Empty"
                    layout_leave_reason.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    val applyLeave=LeaveCreate(type,from_date,to_date,half_day,leave_reason)

                    mService.createLeave(applyLeave).enqueue(object : Callback<BaseResponse<LeaveCreate>> {
                        override fun onFailure(call: Call<BaseResponse<LeaveCreate>>, t: Throwable) {
                            Log.d("applyLeave", "$t")
                        }

                        override fun onResponse(call: Call<BaseResponse<LeaveCreate>>, response: Response<BaseResponse<LeaveCreate>>) {
                            if (response.body()!!.status) {
                                Toast.makeText(this@LeaveActivity,"Your request has been sent.",Toast.LENGTH_LONG).show()
                            } else {
                                Log.d("apply", "Problem")
                            }
                        }
                    })
                }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//        val currentDate = DateFormat.getDateInstance().format(calendar.time)
        val currentDate= "$year-$month-$dayOfMonth"
        if(tagNumber==0){
            start_date.text = Editable.Factory.getInstance().newEditable(currentDate)
        }else{
            end_date.text = Editable.Factory.getInstance().newEditable(currentDate)
        }
    }

    private fun dropDown(context: Context, objects: Array<String>, id: AutoCompleteTextView) {
        id.setAdapter(ArrayAdapter(context, R.layout.dropdown_menu_popup_item, objects))
    }
}
