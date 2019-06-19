package com.incwelltechnology.lms.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.Employee
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_employee_detail.view.*
import kotlinx.android.synthetic.main.list_employee.view.*

class EmployeeAdapter(private val employeeList: ArrayList<Employee>) : RecyclerView.Adapter<EmployeeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_employee, parent, false)
        return EmployeeHolder(v)
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    override fun onBindViewHolder(holder: EmployeeHolder, position: Int) {
        holder.itemView.employee_name.text = employeeList[position].full_name
        holder.itemView.employee_email.text = employeeList[position].email
        Picasso.get()
            .load(employeeList[position].image)
            .placeholder(R.drawable.dummy)
            .error(R.drawable.dummy)
            .into(holder.itemView.employee_image)

        holder.itemView.list_employee.setOnClickListener {

            val employeeDetailDialog = BottomSheetDialog(it.context)
            val view = employeeDetailDialog.layoutInflater.inflate(R.layout.activity_employee_detail, null)

            //populating data in bottomsheet
            Picasso.get()
                .load(employeeList[position].image)
                .placeholder(R.drawable.dummy)
                .error(R.drawable.dummy)
                .into(view.image)
            view.name.text = employeeList[position].full_name
            view.email.text = employeeList[position].email
            view.department.text = employeeList[position].department
            view.phone.text = employeeList[position].phone
            view.leaveIssuer.text = employeeList[position].leave_issuer

            employeeDetailDialog.setContentView(view)
            employeeDetailDialog.show()
        }
    }
}

class EmployeeHolder(itemView: View) : RecyclerView.ViewHolder(itemView)