package com.incwelltechnology.lms.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.Holiday
import kotlinx.android.synthetic.main.list_user_leave.view.*

class HolidayAdapter(private val holidayList: ArrayList<Holiday>) : RecyclerView.Adapter<HolidayHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_public_holidays, parent, false)
        return HolidayHolder(v)
    }

    override fun getItemCount(): Int {
        return holidayList.size
    }

    override fun onBindViewHolder(holder: HolidayHolder, position: Int) {
        holder.itemView.user_at_leave_name.text = holidayList[position].title
    }
}

class HolidayHolder(itemView: View) : RecyclerView.ViewHolder(itemView)