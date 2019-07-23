package com.incwelltechnology.lms.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Leave
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user_at_leave.view.*
import java.util.*

class LeaveAdapter(private val leaveList: ArrayList<Leave>): RecyclerView.Adapter<LeaveAdapter.LeaveViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user_at_leave, parent, false)
        return LeaveViewHolder(v)
    }

    override fun getItemCount(): Int {
        return leaveList.size
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
        val leaveList = leaveList[position]
        holder.setData(leaveList)
    }

    inner class LeaveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(leaveList: Leave?){
            itemView.usr_leave_name.text=leaveList!!.name
            Picasso.get()
                .load(leaveList.image)
                .placeholder(R.drawable.logo1)
                .error(R.drawable.logo1)
                .into(itemView.usr_leave_img)
        }
    }
}