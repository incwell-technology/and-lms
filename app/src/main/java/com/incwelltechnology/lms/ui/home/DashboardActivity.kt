package com.incwelltechnology.lms.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.google.android.material.snackbar.Snackbar
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Birthday
import com.incwelltechnology.lms.databinding.ActivityDashboardBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import com.incwelltechnology.lms.ui.auth.LoginActivity
import com.incwelltechnology.lms.ui.employee.EmployeeActivity
import com.incwelltechnology.lms.ui.home.birthday.BirthdayItem
import com.incwelltechnology.lms.ui.home.fragment.ProfileFragment
import com.incwelltechnology.lms.util.toast
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity<ActivityDashboardBinding>(), NavigationView.OnNavigationItemSelectedListener {

    private val authViewModel: AuthViewModel by viewModel()
    private val homeViewModel:HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.dataBinding.homeViewModel=homeViewModel

        authViewModel.sharedPreference()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //sets color of fab
        fab.backgroundTintList= ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
            com.incwelltechnology.lms.R.string.navigation_drawer_open,
            com.incwelltechnology.lms.R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        homeViewModel.loadBirthday()
        bindUi()
    }

    override fun getLayout(): Int {
        return R.layout.activity_dashboard
    }

    private fun bindUi(){
        homeViewModel.birthdayResponse.observe(this, Observer {
            initRecyclerViewForBirthday(it.toBirthdayItem())
        })
    }

    //birthday
    private fun initRecyclerViewForBirthday(birthdayItem: List<BirthdayItem>) {
        val birthdayAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(birthdayItem)
        }
        recycler_card_birthday.apply {
            layoutManager=LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            setHasFixedSize(true)
            adapter=birthdayAdapter
        }
    }
    private fun List<Birthday>.toBirthdayItem() : List<BirthdayItem>{
        return this.map {
            BirthdayItem(it)
        }
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
                // Handle the my profile action
                addFragment(ProfileFragment(),true,"ProfileFragment")
                toolbar.title="My Profile"
            }
            R.id.nav_users -> {
                // Handle the my profile action
                val intent = Intent(this, EmployeeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)

            }
            R.id.nav_apply_compensation -> {
                // Handle the my profile action

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
