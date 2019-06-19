package com.incwelltechnology.lms.activity

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.App.Companion.context
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.Employee
import com.incwelltechnology.lms.services.AuthenticationService
import com.incwelltechnology.lms.services.BaseResponse
import com.incwelltechnology.lms.services.ServiceBuilder
import com.incwelltechnology.lms.util.EmployeeAdapter
import kotlinx.android.synthetic.main.activity_employee.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class EmployeeActivity : AppCompatActivity() {
    private lateinit var messageIcon: Drawable
    private lateinit var callIcon: Drawable
    private var swipeBackgroundColorRight: ColorDrawable = ColorDrawable(Color.parseColor("#20A6E1"))
    private var swipeBackgroundColorLeft: ColorDrawable = ColorDrawable(Color.parseColor("#20A6E1"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)
        callIcon = ContextCompat.getDrawable(this, R.drawable.ic_local_phone_black_24dp)!!
        messageIcon = ContextCompat.getDrawable(this, R.drawable.ic_email_black_24dp)!!
        val employee = ArrayList<Employee>()

        //toolbar for an activity
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val mService: AuthenticationService = ServiceBuilder
            .buildService(AuthenticationService::class.java)
        recycler_card_employee.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)


        mService.getEmployee()
            .enqueue(object : Callback<BaseResponse<List<Employee>>> {
                override fun onFailure(call: Call<BaseResponse<List<Employee>>>, t: Throwable) {
                    Log.d("testEmployee", "" + t)
                }

                override fun onResponse(
                    call: Call<BaseResponse<List<Employee>>>,
                    response: Response<BaseResponse<List<Employee>>>
                ) {
                    if (response.body()!!.status) {
                        val output = response.body()!!.data
                        if (output != null) {
                            output.indices.forEach { index: Int ->
                                employee.add(
                                    (Employee(
                                        output[index].full_name,
                                        output[index].email,
                                        output[index].phone,
                                        output[index].department,
                                        output[index].leave_issuer,
                                        output[index].image,
                                        output[index].generate_report_access
                                    ))
                                )
                            }
                            val adapter = EmployeeAdapter(employee)

                            recycler_card_employee.apply {
                                this.adapter = adapter
                                layoutManager = LinearLayoutManager(context).apply {
                                    orientation = RecyclerView.VERTICAL
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@EmployeeActivity, "" + response.body()!!.error, Toast.LENGTH_LONG).show()
                    }
                }
            })

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //viewHolder.adapterPosition for identifying employee
                    val pos = viewHolder.adapterPosition
                    if (direction == ItemTouchHelper.RIGHT) {
                        //for sending message
                        sendSMS(employee[pos].phone)
                    } else {
                        //for calling
                        call(employee[pos].phone)
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val iconMargin = (itemView.height - messageIcon.intrinsicHeight) / 2
                    if (dX > 0) {
                        swipeBackgroundColorLeft.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                        messageIcon.setBounds(
                            itemView.left + iconMargin,
                            itemView.top + iconMargin,
                            itemView.left + iconMargin + messageIcon.intrinsicWidth,
                            itemView.bottom - iconMargin
                        )

                    } else {
                        swipeBackgroundColorRight.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                        callIcon.setBounds(
                            itemView.right - iconMargin - callIcon.intrinsicWidth,
                            itemView.top + iconMargin,
                            itemView.right - iconMargin,
                            itemView.bottom - iconMargin
                        )
                    }
                    swipeBackgroundColorLeft.draw(c)
                    swipeBackgroundColorRight.draw(c)
                    c.save()

                    if (dX > 0) {
                        c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                        messageIcon.draw(c)
                    } else {
                        c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                        callIcon.draw(c)
                    }
                    c.restore()
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_card_employee)
    }

    private fun sendSMS(number: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        // At least KitKat
        {
            val defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this) // Need to change the build to API
            val sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("sms:$number"))
            sendIntent.putExtra("sms_body", "")

            if (defaultSmsPackageName != null)
            // Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName)
            }
            startActivity(sendIntent)

        } else
        // For early versions
        {
            val smsIntent = Intent(Intent.ACTION_VIEW)
            smsIntent.type = "vnd.android-dir/mms-sms"
            smsIntent.putExtra("address", number)
            smsIntent.putExtra("sms_body", "message")
            startActivity(smsIntent)
        }
    }

    private fun call(number: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$number")

        if (ActivityCompat.checkSelfPermission(
                this@EmployeeActivity,
                CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("callTest", "No permission granted")
        }
        startActivity(callIntent)
    }
}
