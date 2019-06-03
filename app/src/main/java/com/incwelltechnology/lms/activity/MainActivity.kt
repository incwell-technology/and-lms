package com.incwelltechnology.lms.activity

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.activity.loginActivity.LoginFragment
import com.incwelltechnology.lms.activity.navigationActivity.NavigationHost
import com.incwelltechnology.lms.util.AppUtil


class MainActivity : AppCompatActivity(), NavigationHost {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (AppUtil.isNetworkConnected(this)) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, LoginFragment())
                .commit()
        }else{
            openDialog(this)
        }
    }

    private fun openDialog(context: Context?) {
        val dialog = Dialog(context!!) // Context, this, etc.
        dialog.setContentView(R.layout.alert_dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setTitle("No internet connectivity")
        dialog.show()
    }

    fun exit(){
        this.finish()
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}

