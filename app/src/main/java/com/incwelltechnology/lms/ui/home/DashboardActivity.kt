package com.incwelltechnology.lms.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout.HORIZONTAL
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Birthday
import com.incwelltechnology.lms.data.model.Holiday
import com.incwelltechnology.lms.data.model.Leave
import com.incwelltechnology.lms.databinding.ActivityDashboardBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import com.incwelltechnology.lms.ui.auth.LoginActivity
import com.incwelltechnology.lms.ui.compensation.CompensationActivity
import com.incwelltechnology.lms.ui.employee.EmployeeActivity
import com.incwelltechnology.lms.ui.home.adapter.BirthdayAdapter
import com.incwelltechnology.lms.ui.home.adapter.HolidayAdapter
import com.incwelltechnology.lms.ui.home.adapter.LeaveAdapter
import com.incwelltechnology.lms.ui.home.fragment.ProfileFragment
import com.incwelltechnology.lms.ui.leave.LeaveActivity
import com.incwelltechnology.lms.util.CompareHolidays
import com.incwelltechnology.lms.util.toast
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity<ActivityDashboardBinding>(), NavigationView.OnNavigationItemSelectedListener {

    private val authViewModel: AuthViewModel by viewModel()
    private val homeViewModel:HomeViewModel by viewModel()

    val leave= ArrayList<Leave>()
    val birthday = ArrayList<Birthday>()
    val holiday = ArrayList<Holiday>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.dataBinding.homeViewModel=homeViewModel

        authViewModel.sharedPreference()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //sets color of fab
        fab.backgroundTintList= ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
        fab.setOnClickListener {
            val intent = Intent(this, LeaveActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        //done to get access to view of drawable navigation
        val v: View =navView.getHeaderView(0)
        v.findViewById<TextView>(R.id.nav_user_name).text = authViewModel.user?.full_name
        v.findViewById<TextView>(R.id.nav_user_email).text = authViewModel.user?.email
        val image = v.findViewById<CircularImageView>(R.id.nav_user_image)
        Picasso.get()
            .load(authViewModel.user?.image)
            .placeholder(R.drawable.logo1)
            .error(R.drawable.logo1)
            .into(image)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        recycler_card_leave.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        recycler_card_birthday.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        recycler_public_holidays.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        bindUI()
    }

    override fun getLayout(): Int {
        return R.layout.activity_dashboard
    }

    private fun bindUI() {
        //user at leave recycler view
        homeViewModel.loadUserAtLeave()
        homeViewModel.usrLeaveResponse.observe(this,Observer{
            val output= it
            output.indices.forEach{index:Int ->
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
            val leaveAdapter = LeaveAdapter(leave)
            recycler_card_leave.adapter=leaveAdapter
        })

        //birthday recycler view
        homeViewModel.loadBirthday()
        homeViewModel.birthdayResponse.observe(this,Observer{
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
            val birthdayAdapter = BirthdayAdapter(birthday)
            recycler_card_birthday.adapter=birthdayAdapter
        })

        //holiday recycler view
        homeViewModel.loadHoliday()
        homeViewModel.holidayResponse.observe(this, Observer {
            val output = it
            output.indices.forEach { index: Int ->
                val holidays=output.sortedWith(CompareHolidays)
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
            recycler_public_holidays.adapter=holidayAdapter
        })
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {R.id.action_settings -> {
                toast("Settings")
            }
            R.id.action_logout ->{
                authViewModel.onLogoutButtonClicked()
                this.finish()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return true

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_my_profile -> {
                addFragment(ProfileFragment(),true,"ProfileFragment")
                toolbar.title="My Profile"
            }
            R.id.nav_users -> {
                val intent = Intent(this, EmployeeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)

            }
            R.id.nav_apply_compensation -> {
                val intent=Intent(this, CompensationActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    //adds fragment to stack allowing push and pop
    private fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.add(R.id.replaceFragments, fragment, tag)
        fragmentTransaction.commit()
    }
}
