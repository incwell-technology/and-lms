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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.incwelltechnology.lms.App.Companion.context
import com.incwelltechnology.lms.authenticationServices.AuthenticationService
import com.incwelltechnology.lms.authenticationServices.ServiceBuilder
import com.incwelltechnology.lms.model.Login
import com.incwelltechnology.lms.model.LoginResponse
import com.incwelltechnology.lms.model.Profile
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    val TAG: String = MainActivity::class.java.simpleName
    lateinit var dialog: Dialog
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.incwelltechnology.lms.R.layout.activity_main)

        FirebaseInstanceId
            .getInstance()
            .instanceId
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }
                val token = it.result?.token
                Log.d(TAG, "" + token)
            }

        Hawk.init(context).build()

        if (Hawk.contains("TOKEN")) {
            val value: Profile = Hawk.get("TOKEN")
            val intent = Intent(this@MainActivity, NavigationDrawerActivity::class.java)
            intent.putExtra("user", value)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Welcome back ${value.full_name}", Toast.LENGTH_LONG).show()
        } else {
            login.setOnClickListener {
                //eliminating error hint when editext is filled
                username.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        layout_username.isErrorEnabled = false
                    }
                })
                password.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        layout_password.isErrorEnabled = false
                    }
                })

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

                        // build alert dialog
                        val dialogBuilder = AlertDialog.Builder(this@MainActivity)

                        // set message of alert dialog
                        dialogBuilder.setMessage("Please try again later")
                            // if the dialog is cancelable
                            .setCancelable(false)
                            // negative button text and action
                            .setNegativeButton("ok") { dialog, _ ->
                                dialog.cancel()
                            }
                        // create dialog box
                        val alert = dialogBuilder.create()
                        // set title for alert dialog box
                        alert.setTitle("Something went wrong!")
                        // show alert dialog
                        alert.show()
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
                            progress_horizontal.visibility = View.GONE

                            val dialogBuilder = AlertDialog.Builder(this@MainActivity)
                            dialogBuilder.setMessage("Invalid Credentials!!")
                                .setCancelable(false)
                                .setNegativeButton("Try again") { dialog, _ ->
                                    dialog.cancel()
                                }
                            val alert = dialogBuilder.create()
                            alert.show()
                        }
                    }
                })
            }
        }
    }

    //saving user's credential using hawk
    fun saveCredentials(values: Profile) {
        Hawk.put("TOKEN", values)
    }

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

