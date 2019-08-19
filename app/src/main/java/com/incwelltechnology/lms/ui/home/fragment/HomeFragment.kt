package com.incwelltechnology.lms.ui.home.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Birthday
import com.incwelltechnology.lms.data.model.Holiday
import com.incwelltechnology.lms.data.model.Leave
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import com.incwelltechnology.lms.ui.auth.LoginActivity
import com.incwelltechnology.lms.ui.home.HomeViewModel
import com.incwelltechnology.lms.ui.home.NotificationActivity
import com.incwelltechnology.lms.ui.home.adapter.BirthdayAdapter
import com.incwelltechnology.lms.ui.home.adapter.HolidayAdapter
import com.incwelltechnology.lms.ui.home.adapter.LeaveAdapter
import com.incwelltechnology.lms.util.CompareHolidays
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.show
import com.incwelltechnology.lms.util.toast
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.notification_badge.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {
    private lateinit var image: ImageView
    private lateinit var txtCounter: TextView

    private var status:Boolean = false
    private var newNotification:Boolean = false

    private val authViewModel: AuthViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()

    private var leave = ArrayList<Leave>()
    private var birthday = ArrayList<Birthday>()
    private var holiday = ArrayList<Holiday>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                val extras = intent.extras
                val state = extras?.getBoolean(AppConstants.key)
                status = if (state!!) {
                    txtCounter.visibility = View.VISIBLE
                    true

                } else {
                    false
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.sharedPreference()

        recycler_card_leave.layoutManager = LinearLayoutManager(context, GridLayout.HORIZONTAL, false)
        recycler_card_birthday.layoutManager = LinearLayoutManager(context, GridLayout.HORIZONTAL, false)
        recycler_public_holidays.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        //bind data from viewmodel to activity UI
        bindUI()
        //on pull down refresh the content
        srl_home.setOnRefreshListener {
            bindUI()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)
        //allows image of menu actionLayout attribute to be clicked
        val menuItem: MenuItem = menu.findItem(R.id.action_notification)
        if (menuItem.actionView != null) {
            image = menuItem.actionView.findViewById(R.id.notificationBadge)
            txtCounter = menuItem.actionView.findViewById(R.id.txtCount)
        }
        image.setOnClickListener {
            displayNotificationStack()
            txtCounter.visibility = View.GONE
            Hawk.delete(AppConstants.NOTIFICATION_KEY)
        }
        if (Hawk.contains(AppConstants.NOTIFICATION_KEY)) {
            newNotification = Hawk.get(AppConstants.NOTIFICATION_KEY)
            if(newNotification){
                setNotification()
            }
        }
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                context!!.toast("Setting")
            }
            R.id.action_logout -> {
                authViewModel.onLogoutButtonClicked()
                val intent = Intent(context, LoginActivity::class.java)
                Log.d("context", "$context")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity!!.finish()
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver, IntentFilter(AppConstants.NOTIFICATION_STATE))
    }

    override fun onResume() {
        super.onResume()
        val navView: NavigationView = activity!!.findViewById(R.id.nav_view)
        //sets the first item of navigation menu always higlighted on resume
        navView.menu.getItem(0).isChecked = true
    }

    private fun bindUI() {
        pb_dashboard_loading.show()
        //call function to load data from HomeViewModel
        homeViewModel.loadData()
        //user at leave recycler view
        homeViewModel.usrLeaveResponse.observe(this, Observer {
            tv_error_message.visibility = View.GONE
            pb_dashboard_loading.hide()
            srl_home.isRefreshing = false
            //clear the ArrayList to prevent duplicate same items
            leave.clear()
            for (userLeave in it) {
                leave.add(
                    Leave(
                        userLeave.image,
                        userLeave.name,
                        userLeave.department,
                        userLeave.leave_type,
                        userLeave.half_day
                    )
                )
            }
            if (leave.size < 1) {
                Log.d("size1", "Size is zero")
                recycler_card_leave.visibility = View.GONE
            } else {
                Log.d("size", "there is data")
                val leaveAdapter = LeaveAdapter(leave)
                recycler_card_leave.adapter = leaveAdapter
            }
        })
        //birthday recycler view
        homeViewModel.birthdayResponse.observe(this, Observer {
            birthday.clear()
            for (userBirthday in it) {
                birthday.add(
                    Birthday(
                        userBirthday.full_name,
                        userBirthday.image,
                        userBirthday.department
                    )
                )
            }
            if (birthday.size < 0) {
                recycler_card_birthday.visibility = View.GONE
            } else {
                val birthdayAdapter = BirthdayAdapter(birthday)
                recycler_card_birthday.adapter = birthdayAdapter
            }
        })
        //holiday recycler view
        homeViewModel.holidayResponse.observe(this, Observer {
            holiday.clear()
            val holidays = it.sortedWith(CompareHolidays)
            for (index in 0 until holidays.size) {
                holiday.add(
                    Holiday(
                        holidays[index].title,
                        holidays[index].date,
                        holidays[index].days,
                        holidays[index].image
                    )
                )
            }
            if (holiday.size < 0) {
                recycler_public_holidays.visibility = View.GONE
            } else {
                val holidayAdapter = HolidayAdapter(holiday)
                recycler_public_holidays.adapter = holidayAdapter
            }
        })
        //update UI when error occurs with error message
        homeViewModel.errorResponse.observe(this, Observer {
            pb_dashboard_loading.hide()
            srl_home.isRefreshing = false
            tv_error_message.visibility = View.VISIBLE
            tv_error_message.text = it
        })
    }

    private fun displayNotificationStack() {
        val intent = Intent(context, NotificationActivity::class.java)
        startActivity(intent)
    }

    private fun setNotification(){
        txtCounter.visibility=View.VISIBLE
    }
}
