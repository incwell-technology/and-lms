package com.incwelltechnology.lms.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.Leave
import kotlinx.android.synthetic.main.list_user_leave.view.*

class LeaveAdapter(private val userAtLeaveList:ArrayList<Leave>) : RecyclerView.Adapter<LeaveHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.list_user_leave,parent,false)
        return LeaveHolder(v)
    }

    override fun getItemCount(): Int {
        return userAtLeaveList.size
    }

    override fun onBindViewHolder(holder: LeaveHolder, position: Int) {
        holder.itemView.user_at_leave_name.text = userAtLeaveList[position].name
    }
}
class LeaveHolder(itemView: View) : RecyclerView.ViewHolder(itemView)