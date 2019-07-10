package com.incwelltechnology.lms.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.model.Holiday
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_public_holidays.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class HolidayAdapter(private val holidayList: ArrayList<Holiday>) : RecyclerView.Adapter<HolidayHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.incwelltechnology.lms.R.layout.list_public_holidays, parent, false)
        return HolidayHolder(v)
    }

    override fun getItemCount(): Int {
        return holidayList.size
    }

    override fun onBindViewHolder(holder: HolidayHolder, position: Int) {

        //convert string to date and format
        val dateInString = holidayList[position].date
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateInString)
        val festivalDate : String = DateFormat.getDateInstance(DateFormat.FULL).format(formatter)

        holder.itemView.holiday_name.text=holidayList[position].title
        holder.itemView.holiday_date.text =festivalDate
        holder.itemView.holiday_remaining_day.text= holidayList[position].days.toString()
        Picasso.get()
            .load(holidayList[position].image)
            .placeholder(com.incwelltechnology.lms.R.drawable.dummy)
            .error(com.incwelltechnology.lms.R.drawable.dummy)
            .into(holder.itemView.holiday_image)
    }
}

class HolidayHolder(itemView: View) : RecyclerView.ViewHolder(itemView)