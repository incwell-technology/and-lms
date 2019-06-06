package com.incwelltechnology.lms.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.Holiday

class HolidayAdapter(private val holidayList:ArrayList<Holiday>): RecyclerView.Adapter<HolidayAdapter.HolidayAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayAdapterViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.list_public_holidays,parent,false)
        return HolidayAdapterViewHolder(v)
    }

    override fun getItemCount(): Int {
        return holidayList.size
    }

    override fun onBindViewHolder(holder: HolidayAdapterViewHolder, position: Int) {

    }

    class HolidayAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}