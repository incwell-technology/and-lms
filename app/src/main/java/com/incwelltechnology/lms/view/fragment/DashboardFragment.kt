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
        val birthday = ArrayList<Birthday>()

        val mService: AuthenticationService = ServiceBuilder
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
                            leave.add(
                                (Leave(
                                    output.image,
                                    output.name,
                                    output.department,
                                    output.leave_type,
                                    output.half_day
                                ))
                            )
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
            .enqueue(object : Callback<BaseResponse<List<Birthday>>> {
                override fun onFailure(call: Call<BaseResponse<List<Birthday>>>, t: Throwable) {
                    Log.d("testBirthday", "" + t)
                }

                override fun onResponse(
                    call: Call<BaseResponse<List<Birthday>>>,
                    response: Response<BaseResponse<List<Birthday>>>
                ) {
                    if (response.body()!!.status) {
                        val output = response.body()!!.data
                        if (output != null) {
                            output.indices.forEach { index: Int ->
                                Log.d("testBirthday", output[index].full_name)
                                birthday.add(
                                    (Birthday(
                                        output[index].full_name,
                                        output[index].image,
                                        output[index].department
                                    ))
                                )
                            }
                            val adapter = BirthdayAdapter(birthday)
                            recycler_card_birthday.apply {
                                this!!.adapter = adapter
                                layoutManager = LinearLayoutManager(context).apply {
                                    orientation = LinearLayoutManager.HORIZONTAL
                                }
                            }
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
