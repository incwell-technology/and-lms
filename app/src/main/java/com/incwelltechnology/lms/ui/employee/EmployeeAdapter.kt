package com.incwelltechnology.lms.ui.employee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Employee
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_employee_detail.view.*
import kotlinx.android.synthetic.main.item_employee.view.*

class EmployeeAdapter(
    private var employeeList:ArrayList<Employee>,
    private var employeeListFiltered: ArrayList<Employee>) :

    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>(), Filterable{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(v)
    }

    override fun getItemCount(): Int {
        return employeeListFiltered.size
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val listEmployee = employeeListFiltered[position]
        holder.setData(listEmployee)
    }

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(employee: Employee?) {
            itemView.employee_name.text=employee!!.full_name
            itemView.employee_department.text= employee.department
            Picasso.get()
                .load(employee.image)
                .placeholder(R.drawable.logo1)
                .error(R.drawable.logo1)
                .into(itemView.employee_image)

            itemView.list_employee.setOnClickListener {
                val employeeDetailDialog = BottomSheetDialog(it.context)
                val view = employeeDetailDialog
                    .layoutInflater
                    .inflate(R.layout.activity_employee_detail,null,false)
                //populating data in bottomsheet
                Picasso.get()
                    .load(employee.image)
                    .placeholder(R.drawable.logo1)
                    .error(R.drawable.logo1)
                    .into(view.image)
                view.name.text = employee.full_name
                view.email.text = employee.email
                view.department.text = employee.department
                view.phone.text = employee.phone
                view.leaveIssuer.text = employee.leave_issuer

                //attaching fetched data to bottomsheet and displaying it
                employeeDetailDialog.setContentView(view)
                employeeDetailDialog.show()
            }
        }
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val key=constraint.toString()
                employeeListFiltered = if(key.isEmpty()){
                    employeeList
                }else{
                    val lastFiltered= ArrayList<Employee>()
                    for(row in employeeList ){
                        if(row.full_name.toLowerCase().contains(key.toLowerCase())){
                            lastFiltered.add(row)
                        }
                    }
                    lastFiltered
                }
                val filterResults=FilterResults()
                filterResults.values=employeeListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                employeeListFiltered = results!!.values as ArrayList<Employee>
                notifyDataSetChanged()
            }

        }
    }

    fun updateUI(){
        this.notifyDataSetChanged()
    }

}

