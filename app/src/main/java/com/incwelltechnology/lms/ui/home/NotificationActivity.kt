package com.incwelltechnology.lms.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Notifications
import com.incwelltechnology.lms.databinding.ActivityNotificationBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.ui.home.adapter.NotificationAdapter
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    private val homeViewModel: HomeViewModel by viewModel()
    private var notificationArrayList= ArrayList<Notifications>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //custom toolbar
        setSupportActionBar(custom_toolbar)
        custom_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        custom_toolbar.setNavigationOnClickListener {
            finish()
        }

        recycler_notification.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        notificationBody()
    }
    override fun getLayout(): Int {
        return R.layout.activity_notification
    }
    private fun notificationBody() {
        homeViewModel.loadNotificationList()
        homeViewModel.notificationList.observe(this, Observer {
            notificationArrayList.clear()
            for (notification in it){
                notificationArrayList.add(
                    Notifications(
                        notification.text,
                        notification.date,
                        notification.time
                    )
                )
            }
            if (notificationArrayList.size < 1) {
                Log.d("size1", "Size is zero")
                recycler_notification.visibility = View.GONE
            } else {
                Log.d("size", "there is data")
                val notificationAdapter = NotificationAdapter(notificationArrayList)
                recycler_notification.adapter = notificationAdapter
            }
        })
    }
}
