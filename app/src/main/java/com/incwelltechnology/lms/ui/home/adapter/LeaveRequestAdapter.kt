package com.incwelltechnology.lms.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.RequestLeave
import kotlinx.android.synthetic.main.item_leave_request.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class LeaveRequestAdapter(private val requestList: ArrayList<RequestLeave>,private val listener:MyClickListener) :
    RecyclerView.Adapter<LeaveRequestAdapter.LeaveRequestViewHolder>() {

    lateinit var fromDate: String
    lateinit var toDate: String
    lateinit var halfDay: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveRequestViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_leave_request, parent, false)
        return LeaveRequestViewHolder(v)
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    override fun onBindViewHolder(holder: LeaveRequestViewHolder, position: Int) {
        //convert string to date and format
        val fromDateInString = requestList[position].from_date
        val toDateInString = requestList[position].to_date
        fromDate = convertDate(fromDateInString)
        toDate = convertDate(toDateInString)

        val halfday = requestList[position].half_day
        halfDay = if (halfday) {
            "Half Day"
        } else {
            "Full Day"
        }

        holder.itemView.findViewById<MaterialButton>(R.id.acceptBtn).setOnClickListener {
            listener.accept(position)
        }
        holder.itemView.findViewById<MaterialButton>(R.id.declineBtn).setOnClickListener {
            listener.reject(position)
        }

        val requestList = requestList[position]
        holder.setData(requestList)
    }

    inner class LeaveRequestViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun setData(requestList: RequestLeave?) {
            itemView.requesterFullname.text = requestList!!.full_name
            itemView.requesterDepartment.text = requestList.department
            itemView.leaveType.text = requestList.leave_type
            itemView.from_date.text = fromDate
            itemView.to_date.text = toDate
            itemView.leave_reason.text = requestList.leave_reason
            itemView.half_day.text = halfDay
        }
    }

    interface MyClickListener {
        fun accept(p: Int)
        fun reject(p: Int)
    }

    private fun convertDate(date: String): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(dateFormatter!!)
    }
}