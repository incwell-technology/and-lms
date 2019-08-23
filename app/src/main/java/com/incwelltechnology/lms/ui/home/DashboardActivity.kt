package com.incwelltechnology.lms.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.navigation.NavigationView
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.ui.noticeCreation.CreateNoticeActivity
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivityDashboardBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import com.incwelltechnology.lms.ui.auth.LoginActivity
import com.incwelltechnology.lms.ui.compensation.CompensationActivity
import com.incwelltechnology.lms.ui.employee.EmployeeActivity
import com.incwelltechnology.lms.ui.home.fragment.HomeFragment
import com.incwelltechnology.lms.ui.home.fragment.ProfileFragment
import com.incwelltechnology.lms.ui.leave.LeaveActivity
import com.incwelltechnology.lms.ui.userRegistration.UserRegistrationActivity
import com.incwelltechnology.lms.util.toast
import com.mikhaellopez.circularimageview.CircularImageView
import com.orhanobut.hawk.Hawk
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity<ActivityDashboardBinding>(),
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var image: ImageView
    private lateinit var txtCounter: TextView

    private var status: Boolean = false
    private var newNotification: Boolean = false

    private val authViewModel: AuthViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()

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
        super.dataBinding.homeViewModel = homeViewModel
        authViewModel.sharedPreference()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        replaceFragment(HomeFragment(), false, "HomeFragment")

        //sets color of fab
        fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
        fab.setOnClickListener {
            val intent = Intent(this, LeaveActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        //done to get access to view of drawable navigation
        val v: View = navView.getHeaderView(0)
        v.findViewById<TextView>(R.id.nav_user_name).text = authViewModel.user?.full_name
        v.findViewById<TextView>(R.id.nav_user_email).text = authViewModel.user?.email
        val image = v.findViewById<CircularImageView>(R.id.nav_user_image)

        Picasso.get()
            .load(authViewModel.user!!.image)
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
        navView.menu.getItem(0).isChecked = true
    }

    override fun getLayout(): Int {
        return R.layout.activity_dashboard
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter(AppConstants.NOTIFICATION_STATE))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
            if (newNotification) {
                setNotification()
            }
        }
        super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                this.toast("Setting")
            }
            R.id.action_logout -> {
                authViewModel.onLogoutButtonClicked()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                this.finish()
            }
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment(), false, "HomeFragment")
                item.isChecked = true
                toolbar.title = "Home"
            }

            R.id.nav_my_profile -> {
                replaceFragment(ProfileFragment(), true, "ProfileFragment")
                item.isChecked = true
                toolbar.title = "My Profile"

            }
            R.id.nav_users -> {
                val intent = Intent(this, EmployeeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
            }
            R.id.nav_apply_compensation -> {
                val intent = Intent(this, CompensationActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
            }

            R.id.nav_register_user -> {
                val intent = Intent(this,UserRegistrationActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left)
            }
            R.id.nav_create_notice -> {
                val intent=Intent(this,
                    CreateNoticeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displayNotificationStack() {
        val intent = Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }

    private fun setNotification() {
        txtCounter.visibility = View.VISIBLE
    }

    //adds fragment to stack allowing push and pop
    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.replace(R.id.replaceFragments, fragment, tag)
        fragmentTransaction.commit()
    }
}

