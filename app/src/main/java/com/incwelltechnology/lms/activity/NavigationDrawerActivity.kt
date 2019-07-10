package com.incwelltechnology.lms.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.incwelltechnology.lms.App
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.Profile
import com.incwelltechnology.lms.view.fragment.DashboardFragment
import com.incwelltechnology.lms.view.fragment.UserFragment
import com.mikhaellopez.circularimageview.CircularImageView
import com.orhanobut.hawk.Hawk
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.toolbar.*


class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mUser: Profile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)

        Hawk.init(App.context).build()

        //makes DashboardFragment as default landing fragment
        addFragment(DashboardFragment(), true, "DashboardFragment")


        mUser = intent.getParcelableExtra("user")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, LeaveActivity::class.java)
            startActivity(intent)
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)

        //done to get access to view of drawable navigation
        val v: View = navView.getHeaderView(0)
        v.findViewById<TextView>(R.id.nav_user_name).text = mUser.full_name
        v.findViewById<TextView>(R.id.nav_user_email).text = mUser.email
        val image = v.findViewById<CircularImageView>(R.id.nav_user_image)
        Picasso.get()
            .load(mUser.image)
            .placeholder(R.drawable.dummy)
            .error(R.drawable.dummy)
            .into(image)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        //sets menu item activated on start itself
        navView.menu.getItem(0).isChecked = true
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // build alert dialog
            val dialogBuilder = AlertDialog.Builder(this)
            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to close this application ?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Proceed") { _, _ -> finish()
                }
                // negative button text and action
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel()
                }
            // create dialog box
            val alert = dialogBuilder.create()
            // show alert dialog
            alert.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this,"Clicked on Setting",Toast.LENGTH_LONG).show()
            }
            R.id.action_logout ->{
                Hawk.delete("TOKEN")
                this.finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                addFragment(DashboardFragment(), true, "DashboardFragment")
                toolbar.title = "Home"
            }
            R.id.nav_my_profile -> {
                addFragment(UserFragment.getInstance(mUser), true, "UserFragment")
                toolbar.title = "My Profile"
            }
            R.id.nav_users -> {
                val intent = Intent(this, EmployeeActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_apply_compensation -> {
                val intent = Intent(this,CompensationActivity::class.java)
                startActivity(intent)
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
        fragmentTransaction.replace(R.id.replaceFragments, fragment, tag)
        fragmentTransaction.commit()
    }
}
