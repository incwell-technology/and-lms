package com.incwelltechnology.lms.activity

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.incwelltechnology.lms.R
import kotlinx.android.synthetic.main.activity_leave.*

class LeaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave)

        val holiday = arrayOf("Half Day", "Full Day")
        val type = arrayOf("Annual Leave", "Sick Leave", "Compensation Leave", "Unpaid Leave")
        dropDown(this, holiday, leave_dropdown)
        dropDown(this, type, type_of_leave_dropdown)

        //toolbar for an activity
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun dropDown(context: Context, objects: Array<String>, id: AutoCompleteTextView) {
        id.setAdapter(ArrayAdapter(context, R.layout.dropdown_menu_popup_item, objects))
    }
}
