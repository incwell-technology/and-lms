package com.incwelltechnology.lms.activity

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.incwelltechnology.lms.R
import kotlinx.android.synthetic.main.activity_leave.*


class LeaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave)
        val HOLIDAY = arrayOf("Half Day", "Full Day")
        val TYPE = arrayOf("Annual Leave","Sick Leave","Compensation Leave","Unpaid Leave")
        dropDown(this,HOLIDAY,leave_dropdown)
        dropDown(this,TYPE,type_of_leave_dropdown)
    }
    private fun dropDown(context:Context, objects:Array<String>,id:AutoCompleteTextView){
        id.setAdapter(ArrayAdapter(context,R.layout.dropdown_menu_popup_item,objects))
    }
}
