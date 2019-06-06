package com.incwelltechnology.lms.view.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.incwelltechnology.lms.model.Birthday
import com.incwelltechnology.lms.model.Leave
import com.incwelltechnology.lms.services.AuthenticationService
import com.incwelltechnology.lms.services.BaseResponse
import com.incwelltechnology.lms.services.ServiceBuilder
import com.incwelltechnology.lms.util.BirthdayAdapter
import com.incwelltechnology.lms.util.LeaveAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardFragment : Fragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val leave = ArrayList<Leave>()
        val birthday=ArrayList<Birthday>()
        var mService: AuthenticationService = ServiceBuilder
            .buildService(AuthenticationService::class.java)
        super.onActivityCreated(savedInstanceState)
        recycler_card_leave.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //for getting list of users who are at leave
        mService.getLeaveToday()
            .enqueue(object : Callback<BaseResponse<Leave>> {
                override fun onFailure(call: Call<BaseResponse<Leave>>, t: Throwable) {

                }

                override fun onResponse(call: Call<BaseResponse<Leave>>, response: Response<BaseResponse<Leave>>) {
                    if (response.body()!!.status) {
                        val output = response.body()!!.data
                        if (output != null) {
                            leave.add((Leave(output.image, output.name,output.department,output.leave_type,output.half_day)))
                            val adapter = LeaveAdapter(leave)
                            recycler_card_leave.adapter = adapter
                        }
                    } else {
                        Toast.makeText(activity, "" + response.body()!!.error, Toast.LENGTH_LONG).show()
                    }
                }

            })

        //for getting list of birthdays
        mService.getBirthday()
            .enqueue(object : Callback<BaseResponse<Birthday>> {
                override fun onFailure(call: Call<BaseResponse<Birthday>>, t: Throwable) {

                }

                override fun onResponse(call: Call<BaseResponse<Birthday>>, response: Response<BaseResponse<Birthday>>) {
                    if (response.body()!!.status) {
                        val output = response.body()!!.data
                        if (output != null) {
                            birthday.add((Birthday(output.full_name,output.image,output.department)))
                            val adapter = BirthdayAdapter(birthday)
                            recycler_card_leave.adapter = adapter
                            Log.d("testBirthday", output.full_name)
                        }
                    } else {
                        Toast.makeText(activity, "" + response.body()!!.error, Toast.LENGTH_LONG).show()
                    }
                }
            })

        //for getting list of holidays

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.incwelltechnology.lms.R.layout.fragment_dashboard, container, false)
    }
}
