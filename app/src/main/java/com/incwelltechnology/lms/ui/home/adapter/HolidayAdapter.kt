package com.incwelltechnology.lms.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Holiday
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_public_holiday.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HolidayAdapter(private val holidayList: ArrayList<Holiday>): RecyclerView.Adapter<HolidayAdapter.DashboardViewHolder>() {
    lateinit var festivalDate:String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_public_holiday, parent, false)
        return DashboardViewHolder(v)
    }

    override fun getItemCount(): Int {
        return holidayList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        //convert string to date and format
        val dateInString = holidayList[position].date
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateInString)
        festivalDate = DateFormat.getDateInstance(DateFormat.FULL).format(formatter!!)

        val holidayList = holidayList[position]
        holder.setData(holidayList)
    }

    inner class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(holidayList: Holiday?){
            itemView.holiday_name.text=holidayList!!.title
            itemView.holiday_date.text=festivalDate
            itemView.holiday_remaining_day.text=holidayList.days.toString()
            Picasso.get()
                .load(holidayList.image)
                .placeholder(R.drawable.logo1)
                .error(R.drawable.logo1)
                .into(itemView.imageHoliday)
        }
    }
}