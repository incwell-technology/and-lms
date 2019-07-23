package com.incwelltechnology.lms.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Birthday
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user_birthday.view.*
import java.util.*

class BirthdayAdapter(private val birthdayList: ArrayList<Birthday>): RecyclerView.Adapter<BirthdayAdapter.BirthdayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthdayViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user_birthday, parent, false)
        return BirthdayViewHolder(v)
    }

    override fun getItemCount(): Int {
        return birthdayList.size
    }

    override fun onBindViewHolder(holder: BirthdayViewHolder, position: Int) {
        val birthdayList = birthdayList[position]
        holder.setData(birthdayList)
    }

    inner class BirthdayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(birthdayList:Birthday?){
            itemView.user_birthday_name.text=birthdayList!!.full_name
            Picasso.get()
                .load(birthdayList.image)
                .placeholder(R.drawable.logo1)
                .error(R.drawable.logo1)
                .into(itemView.imageBirthday)
        }
    }
}