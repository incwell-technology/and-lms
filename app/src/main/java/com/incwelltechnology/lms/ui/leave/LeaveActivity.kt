package com.incwelltechnology.lms.ui.leave

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivityLeaveBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.ui.home.fragment.DatePickerFragment
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.show
import com.incwelltechnology.lms.util.snack
import kotlinx.android.synthetic.main.activity_leave.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class LeaveActivity : BaseActivity<ActivityLeaveBinding>(), DatePickerDialog.OnDateSetListener {

    var tagNumber: Int = 1
    private val leaveViewModel: LeaveViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.dataBinding.leave = leaveViewModel

        val holiday = arrayOf("Half Day", "Full Day")
        val type = arrayOf("Annual Leave", "Sick Leave", "CompensationActivity Leave", "Unpaid Leave")
        dropDown(this, holiday, leave_dropdown)
        dropDown(this, type, type_of_leave_dropdown)

        start_date.setOnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "start date picker")
            tagNumber = 0
        }
        end_date.setOnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "end date picker")
            tagNumber = 1
        }

        apply_leave.setOnClickListener {
            val type = type_of_leave_dropdown.text.toString()
            val from_date = start_date.text.toString()
            val to_date = end_date.text.toString()
            val leave = leave_dropdown.text.toString()
            val leave_reason = reason.text.toString()

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
                    layout_duration.error = "Duration Field is Empty"
                    layout_duration.requestFocus()
                    return@setOnClickListener
                }
                type.isEmpty() -> {
                    layout_type_of_leave.error = "Leave Type Field is Empty"
                    layout_type_of_leave.requestFocus()
                    return@setOnClickListener
                }
                leave_reason.isEmpty() -> {
                    layout_leave_reason.error = "Leave Reason Field is Empty"
                    layout_leave_reason.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    leaveViewModel.getValues(type, from_date, to_date, leave, leave_reason)
                    leaveViewModel.onApplyButtonClick()
                    processing.show()
                    val message: LiveData<String> = leaveViewModel.message
                    message.observe(this, androidx.lifecycle.Observer {
                        processing.hide()
                        apply_leave.snack("${message.value}")
                        clearField()
                    })
                }
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_leave
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//        val currentDate = DateFormat.getDateInstance().format(calendar.time)
        val currentDate = "$year-$month-$dayOfMonth"
        if (tagNumber == 0) {
            start_date.text = Editable.Factory.getInstance().newEditable(currentDate)
        } else {
            end_date.text = Editable.Factory.getInstance().newEditable(currentDate)
        }
    }

    private fun dropDown(context: Context, objects: Array<String>, id: AutoCompleteTextView) {
        id.setAdapter(ArrayAdapter(context, R.layout.dropdown_menu_popup_item, objects))
    }

    private fun clearField(){
        type_of_leave_dropdown.text.clear()
        start_date.text?.clear()
        end_date.text?.clear()
        leave_dropdown.text.clear()
        reason.text?.clear()
    }
}
