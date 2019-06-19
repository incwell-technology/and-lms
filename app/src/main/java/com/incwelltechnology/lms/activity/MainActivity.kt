package com.incwelltechnology.lms.activity

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.incwelltechnology.lms.App.Companion.context
import com.incwelltechnology.lms.model.Login
import com.incwelltechnology.lms.model.LoginResponse
import com.incwelltechnology.lms.model.Profile
import com.incwelltechnology.lms.services.AuthenticationService
import com.incwelltechnology.lms.services.ServiceBuilder
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var dialog: Dialog
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.incwelltechnology.lms.R.layout.activity_main)

        Hawk.init(context).build()

        if (Hawk.contains("TOKEN")) {
            val value:Profile = Hawk.get("TOKEN")
            val intent = Intent(this@MainActivity, NavigationDrawerActivity::class.java)
            intent.putExtra("user", value)
            startActivity(intent)
            finish()
//            Hawk.delete("TOKEN")
            Log.d("token","$value")
            Toast.makeText(this,"Welcome back ${value.full_name}",Toast.LENGTH_LONG).show()
        } else {
            login.setOnClickListener {
                val username = username.text.toString()
                val password = password.text.toString()

                if (username.isEmpty()) {
                    layout_username.error = "Usename is Empty"
                    layout_username.requestFocus()
                    return@setOnClickListener
                }
                if (password.isEmpty()) {
                    layout_password.error = "Password is Empty"
                    layout_password.requestFocus()
                    return@setOnClickListener
                }
                progress_horizontal.max = 100
                progress_horizontal.isIndeterminate = true
                progress_horizontal.visibility = View.VISIBLE

                val login = Login(username, password)
                val service = ServiceBuilder.buildService(AuthenticationService::class.java)
                service.userLogin(login).enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        progress_horizontal.visibility = View.GONE
                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.body()?.status == true) {
                            progress_horizontal.visibility = View.GONE
                            saveCredentials(response.body()!!.data)
                            val intent = Intent(this@MainActivity, NavigationDrawerActivity::class.java)
                            intent.putExtra("user", response.body()!!.data)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, response.body()?.error, Toast.LENGTH_LONG).show()
                            progress_horizontal.visibility = View.GONE
                        }
                    }
                })
            }
        }
    }

    //saving user's credential using hawk
    fun saveCredentials(values:Profile){
        Hawk.put("TOKEN",values)
    }

    //for saving user's token when login is successful
//    private fun saveCredentials(token: String) {
//        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
//        val editor = sharedPref.edit()
//        editor.putString("TOKEN", token)
//            .apply()
//    }

    private val wifiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            if (isConnected) {

                if (::dialog.isInitialized) {
                    dialog.dismiss()
                }
            } else {
                Toast.makeText(this@MainActivity, "No Internet Connection", Toast.LENGTH_LONG).show()
                openDialog(this@MainActivity)
            }
        }
    }

    private fun openDialog(context: Context?) {
        dialog = Dialog(context!!) // Context, this, etc.
        dialog.setContentView(com.incwelltechnology.lms.R.layout.alert_dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setTitle("No internet connectivity")
        dialog.show()
    }

    fun exit() {
        this.finish()
    }


    override fun onResume() {
        super.onResume()
        registerReceiver(wifiReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(wifiReceiver)
    }


}

