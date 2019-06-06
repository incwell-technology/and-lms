package com.incwelltechnology.lms.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.Birthday

class BirthdayAdapter(private val userBirthdayList:ArrayList<Birthday>): RecyclerView.Adapter<BirthdayHolder>()   {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthdayHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.list_user_birthday,parent,false)
        return BirthdayHolder(v)
    }

    override fun getItemCount(): Int {
        return userBirthdayList.size
    }

    override fun onBindViewHolder(holder: BirthdayHolder, position: Int) {

    }
}
class BirthdayHolder(itemView: View) : RecyclerView.ViewHolder(itemView)