package com.incwelltechnology.lms.adapters

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

class EmployeeAdapter(private val employeeList: ArrayList<Employee>) : RecyclerView.Adapter<EmployeeAdapter.EmployeeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_employee, parent, false)
        return EmployeeHolder(v)
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    override fun onBindViewHolder(holder: EmployeeHolder, position: Int) {
        val employeeList=employeeList[position]
        holder.setData(employeeList)
    }

    inner class EmployeeHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setData(employeeList: Employee?){
            itemView.employee_name.text = employeeList!!.full_name
            itemView.employee_email.text = employeeList.email
            Picasso.get()
                .load(employeeList.image)
                .placeholder(R.drawable.dummy)
                .error(R.drawable.dummy)
                .into(itemView.employee_image)

            itemView.list_employee.setOnClickListener {
                val employeeDetailDialog = BottomSheetDialog(it.context)
                val view = employeeDetailDialog.layoutInflater.inflate(R.layout.activity_employee_detail,null,false)
                //populating data in bottomsheet
                Picasso.get()
                    .load(employeeList.image)
                    .placeholder(R.drawable.dummy)
                    .error(R.drawable.dummy)
                    .into(view.image)
                view.name.text = employeeList.full_name
                view.email.text = employeeList.email
                view.department.text = employeeList.department
                view.phone.text = employeeList.phone
                view.leaveIssuer.text = employeeList.leave_issuer

                //attaching fetched data to bottomsheet and displaying it
                employeeDetailDialog.setContentView(view)
                employeeDetailDialog.show()
            }
        }
    }
}