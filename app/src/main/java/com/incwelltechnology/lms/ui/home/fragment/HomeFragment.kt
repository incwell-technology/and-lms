package com.incwelltechnology.lms.ui.home.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Birthday
import com.incwelltechnology.lms.data.model.Holiday
import com.incwelltechnology.lms.data.model.Leave
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import com.incwelltechnology.lms.ui.auth.LoginActivity
import com.incwelltechnology.lms.ui.home.HomeViewModel
import com.incwelltechnology.lms.ui.home.adapter.BirthdayAdapter
import com.incwelltechnology.lms.ui.home.adapter.HolidayAdapter
import com.incwelltechnology.lms.ui.home.adapter.LeaveAdapter
import com.incwelltechnology.lms.util.CompareHolidays
import com.incwelltechnology.lms.util.toast
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private val authViewModel: AuthViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()

    val leave = ArrayList<Leave>()
    private val birthday = ArrayList<Birthday>()
    private val holiday = ArrayList<Holiday>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.sharedPreference()

        recycler_card_leave.layoutManager = LinearLayoutManager(context, GridLayout.HORIZONTAL, false)
        recycler_card_birthday.layoutManager = LinearLayoutManager(context, GridLayout.HORIZONTAL, false)
        recycler_public_holidays.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        bindUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)
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
                startActivity (intent)
            }
        }
        return true

    }

    override fun onResume() {
        super.onResume()
        val navView: NavigationView = activity!!.findViewById(R.id.nav_view)
        navView.menu.getItem(0).isChecked = true
    }

    private fun bindUI() {
        //user at leave recycler view
        homeViewModel.loadUserAtLeave()
        homeViewModel.usrLeaveResponse.observe(this, Observer {
            val output = it
            output.indices.forEach { index: Int ->
                leave.add(
                    Leave(
                        leave[index].name,
                        leave[index].image,
                        leave[index].department,
                        leave[index].leave_type,
                        leave[index].half_day
                    )
                )
            }
            if (leave.size < 1) {
                Log.d("size", "Size is zero")
                recycler_card_leave.visibility = View.GONE
            } else {
                Log.d("size", "there is data")
                val leaveAdapter = LeaveAdapter(leave)
                recycler_card_leave.adapter = leaveAdapter
            }


        })

        //birthday recycler view
        homeViewModel.loadBirthday()
        homeViewModel.birthdayResponse.observe(this, Observer {
            val output = it
            output.indices.forEach { index: Int ->
                birthday.add(
                    Birthday(
                        birthday[index].full_name,
                        birthday[index].image,
                        birthday[index].department
                    )
                )
            }
            if (leave.size < 1) {
                Log.d("size", "Size is zero")
                recycler_card_birthday.visibility = View.GONE
            } else {
                Log.d("size", "there is data")
                val birthdayAdapter = BirthdayAdapter(birthday)
                recycler_card_birthday.adapter = birthdayAdapter
            }
        })

        //holiday recycler view
        homeViewModel.loadHoliday()
        homeViewModel.holidayResponse.observe(this, Observer {
            val output = it
            output.indices.forEach { index: Int ->
                val holidays = output.sortedWith(CompareHolidays)
                holiday.add(
                    Holiday(
                        holidays[index].title,
                        holidays[index].date,
                        holidays[index].days,
                        holidays[index].image
                    )
                )
            }
            if (leave.size < 1) {
                Log.d("size", "Size is zero")
                recycler_public_holidays.visibility = View.GONE
            } else {
                Log.d("size", "there is data")
                val holidayAdapter = HolidayAdapter(holiday)
                recycler_public_holidays.adapter = holidayAdapter
            }
        })
    }


}
