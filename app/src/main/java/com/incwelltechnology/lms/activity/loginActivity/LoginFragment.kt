package com.incwelltechnology.lms.activity.loginActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.activity.navigationActivity.NavigationDrawer
import com.incwelltechnology.lms.model.LoginResponse
import com.incwelltechnology.lms.model.User
import com.incwelltechnology.lms.services.AuthenticationService
import com.incwelltechnology.lms.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_login_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_login_fragment, container, false)

        view.login.setOnClickListener {
            val username = view.username.text.toString()
            val password = view.password.text.toString()

            if (username.isEmpty()) {
                view.layout_username.error = "Usename is Empty"
                view.layout_username.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                view.layout_password.error = "Password is Empty"
                view.layout_password.requestFocus()
                return@setOnClickListener
            }

            val user = User(username, password)


            val service = ServiceBuilder.buildService(AuthenticationService::class.java)
            service.userLogin(user).enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
//                    val intent=Intent(activity, UsersFragment::class.java)
//                    startActivity(intent)
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                    if (response.body()?.status == true) {
                        val intent = Intent(activity, NavigationDrawer::class.java)
                        intent.putExtra("user", response.body()!!.data)
                        startActivity(intent)
                    } else {
                        Toast.makeText(activity, response.body()?.error, Toast.LENGTH_LONG).show()
                    }

                }
            })
        }
        return view
    }
}
