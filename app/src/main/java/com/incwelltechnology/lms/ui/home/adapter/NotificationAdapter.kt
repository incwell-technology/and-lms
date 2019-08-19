package com.incwelltechnology.lms.ui.home.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Notifications
import kotlinx.android.synthetic.main.item_notification.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class NotificationAdapter(private val notificationList: ArrayList<Notifications>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    lateinit var notificationDate:String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(v)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        //convert string to date and format
        val dateInString = notificationList[position].date
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateInString)
        notificationDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(formatter!!)


        val notificationList = notificationList[position]
        holder.setData(notificationList)
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(notificationList: Notifications?) {
            val colorSet1 = listOf(
                "#3497DA", "#9E5FBC", "#D23D53", "#FC8A3A"
            )
            val colorSet2 = listOf(
                "#22578B", "#7153AB", "#D2342D", "#F84F38"
            )
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.parseColor(colorSet1[Random.nextInt(4)]),
                    Color.parseColor(colorSet2[Random.nextInt(4)])
                )
            )

            itemView.notification_card.background = gradientDrawable
            itemView.notification_text.text = notificationList?.text
            itemView.notification_date.text = notificationDate
            itemView.notification_time.text = notificationList?.time
        }
    }
}