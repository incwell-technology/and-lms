package com.incwelltechnology.lms.ui.employee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Employee
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_employee_detail.view.*
import kotlinx.android.synthetic.main.item_employee.view.*

class EmployeeAdapter(private val employeeList: ArrayList<Employee>) :
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(v)
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employeeList = employeeList[position]
        holder.setData(employeeList)
    }

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(employeeList: Employee?) {
            itemView.employee_name.text=employeeList!!.full_name
            itemView.employee_email.text= employeeList.email
            Picasso.get()
                .load(employeeList.image)
                .placeholder(R.drawable.logo1)
                .error(R.drawable.logo1)
                .into(itemView.employee_image)

            itemView.list_employee.setOnClickListener {
                val employeeDetailDialog = BottomSheetDialog(it.context)
                val view = employeeDetailDialog.layoutInflater.inflate(R.layout.activity_employee_detail,null,false)
                //populating data in bottomsheet
                Picasso.get()
                    .load(employeeList.image)
                    .placeholder(R.drawable.logo1)
                    .error(R.drawable.logo1)
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
    fun updateUI(){
        this.notifyDataSetChanged()
    }


}

