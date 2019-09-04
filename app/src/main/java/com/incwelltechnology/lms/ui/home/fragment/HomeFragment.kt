package com.incwelltechnology.lms.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Birthday
import com.incwelltechnology.lms.data.model.Holiday
import com.incwelltechnology.lms.data.model.Leave
import com.incwelltechnology.lms.data.model.RequestLeave
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import com.incwelltechnology.lms.ui.home.HomeViewModel
import com.incwelltechnology.lms.ui.home.adapter.BirthdayAdapter
import com.incwelltechnology.lms.ui.home.adapter.HolidayAdapter
import com.incwelltechnology.lms.ui.home.adapter.LeaveAdapter
import com.incwelltechnology.lms.ui.home.adapter.LeaveRequestAdapter
import com.incwelltechnology.lms.util.CompareHolidays
import com.incwelltechnology.lms.util.Coroutine
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.show
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()

    private var leaveRequest = ArrayList<RequestLeave>()
    private var leave = ArrayList<Leave>()
    private var birthday = ArrayList<Birthday>()
    private var holiday = ArrayList<Holiday>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.sharedPreference()

        recycler_leave_request.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        recycler_card_leave.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        recycler_card_birthday.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        recycler_public_holidays.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        bindUI() //bind data from viewmodel to activity UI
        //on pull down refresh the content
        srl_home.setOnRefreshListener {
            refreshFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        val navView: NavigationView = activity!!.findViewById(R.id.nav_view)
        //sets the first item of navigation menu always higlighted on resume
        navView.menu.getItem(0).isChecked = true
    }

    private fun bindUI() {
        pb_dashboard_loading.show()
        homeViewModel.loadData() //call function to load data from HomeViewModel
        //leave request recycler view
        homeViewModel.usrLeaveRequest.observe(this, Observer {
            leaveRequest.clear() //clear the ArrayList to prevent duplicate same items
            for (request in it) {
                leaveRequest.add(
                    RequestLeave(
                        request.id,
                        request.full_name,
                        request.department,
                        request.from_date,
                        request.to_date,
                        request.total_days,
                        request.leave_type,
                        request.leave_reason,
                        request.half_day,
                        request.notification
                    )
                )
            }
            if (leaveRequest.size >= 1) {
                val leaveRequestAdapter =
                    LeaveRequestAdapter(leaveRequest, object : LeaveRequestAdapter.MyClickListener {
                        override fun accept(p: Int) {
                            homeViewModel.leaveId = it[p].id
                            homeViewModel.onAcceptBtnClicked()
                            pb_dashboard_loading.show()
                            homeViewModel.messageResponse.observe(
                                this@HomeFragment,
                                Observer { message ->
                                    pb_dashboard_loading.hide()
                                    Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
                                    Coroutine.io {
                                        delay(2000L)
                                        refreshFragment()
                                    }
                                })
                        }

                        override fun reject(p: Int) {
                            homeViewModel.leaveId = it[p].id
                            homeViewModel.onRejectBtnClicked()
                            pb_dashboard_loading.show()
                            homeViewModel.messageResponse.observe(
                                this@HomeFragment,
                                Observer { message ->
                                    pb_dashboard_loading.hide()
                                    Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
                                })
                            refreshFragment()
                        }
                    })
                recycler_leave_request.adapter = leaveRequestAdapter
            }
        })

        //user at leave recycler view
        homeViewModel.usrLeaveResponse.observe(this, Observer {
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
                mtrlCard_Leave.visibility = View.GONE
            } else {
                mtrlCard_Leave.visibility = View.VISIBLE
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
            if (birthday.size < 1) {
                mtrlCard_birthday.visibility = View.GONE
            } else {
                val birthdayAdapter = BirthdayAdapter(birthday)
                recycler_card_birthday.adapter = birthdayAdapter
            }
        })

        //holiday recycler view
        homeViewModel.holidayResponse.observe(this, Observer {
            showStatus()
            holiday.clear()
            val holidays = it.sortedWith(CompareHolidays)
            for (index in holidays.indices) {
                holiday.add(
                    Holiday(
                        holidays[index].title,
                        holidays[index].date,
                        holidays[index].days,
                        holidays[index].image
                    )
                )
            }
            val holidayAdapter = HolidayAdapter(holiday)
            recycler_public_holidays.adapter = holidayAdapter
        })

        //update UI when error occurs with error message
        homeViewModel.errorResponse.observe(this, Observer {
            showStatus()
            tv_error_message.text = it
        })
    }

    private fun showStatus() {
        if (tv_error_message.visibility == View.VISIBLE) {
            tv_error_message.visibility = View.GONE
        } else {
            tv_error_message.visibility = View.VISIBLE
        }
        pb_dashboard_loading.hide()
        srl_home.isRefreshing = false
    }

    private fun refreshFragment() {
        val currentFragment = fragmentManager?.findFragmentByTag("HomeFragment")
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.detach(currentFragment!!)
        fragmentTransaction?.attach(currentFragment!!)
        fragmentTransaction?.commit()
    }
}
