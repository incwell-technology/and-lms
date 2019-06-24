package com.incwelltechnology.lms.view.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment:DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar=Calendar.getInstance()
        val year:Int=calendar.get(Calendar.YEAR)
        val month:Int=calendar.get(Calendar.MONTH)
        val day:Int=calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity,activity as DatePickerDialog.OnDateSetListener,year,month,day)
    }

}