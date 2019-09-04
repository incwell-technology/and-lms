package com.incwelltechnology.lms.ui.home.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.RequestLeave
import java.util.ArrayList

class MyAdapter(private var context: Context, private var requestList: List<RequestLeave>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return requestList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_leave_request, container, false)
        val requesterFullName = view.findViewById<TextView>(R.id.requesterFullname)
        val requesterDepartment = view.findViewById<TextView>(R.id.requesterDepartment)
        val requesterImage = view.findViewById<ImageView>(R.id.requesterImage)
        requesterFullName.text = requestList[position].full_name
        requesterDepartment.text = requestList[position].department

        container.addView(view,0)
        return view
    }

}