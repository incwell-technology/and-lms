package com.incwelltechnology.lms.ui.employee

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Employee
import com.incwelltechnology.lms.databinding.ActivityEmployeeBinding
import com.incwelltechnology.lms.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_employee.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class EmployeeActivity : BaseActivity<ActivityEmployeeBinding>() {
    private lateinit var adapter: EmployeeAdapter
    private lateinit var messageIcon: Drawable
    private lateinit var callIcon: Drawable
    private lateinit var phoneNumber: String
    private var swipeBackgroundColorRight: ColorDrawable = ColorDrawable(Color.parseColor("#E0E0E0"))
    private var swipeBackgroundColorLeft: ColorDrawable = ColorDrawable(Color.parseColor("#E0E0E0"))
    val employee = ArrayList<Employee>()
    private val employeeViewModel: EmployeeViewModel by viewModel()

    override fun getLayout(): Int {
        return R.layout.activity_employee
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callIcon = ContextCompat.getDrawable(this, R.drawable.ic_phone_black_24dp)!!
        messageIcon = ContextCompat.getDrawable(this, R.drawable.ic_email_black_24dp)!!

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recycler_card_employee.layoutManager = layoutManager

        bindUI()

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
                        //for sending mail
                        sendMail(employee[pos].email)
                    } else {
                        //for calling
                        phoneNumber = employee[pos].phone
                        call(phoneNumber)
                    }
                    adapter.updateUI()
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

    private fun bindUI() {
        employeeViewModel.loadEmployee()
        employeeViewModel.employeeResponse.observe(this, Observer {
            val output = it
            output.indices.forEach { index: Int ->
                employee.add(
                    Employee(
                        output[index].full_name,
                        output[index].email,
                        output[index].phone,
                        output[index].department,
                        output[index].leave_issuer,
                        output[index].image,
                        output[index].generate_report_access
                    )
                )
            }
            adapter = EmployeeAdapter(employee)
            recycler_card_employee.adapter = adapter

        })
    }

    private fun sendMail(mail: String) {
        val aEmailList = arrayOf(mail)
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, aEmailList)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        emailIntent.type = "email/MIME"
        emailIntent.putExtra(Intent.EXTRA_TEXT, "")
        startActivity(Intent.createChooser(emailIntent, "Complete action using"))
    }

    private fun call(number: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$number")
        if (ContextCompat.checkSelfPermission(this, CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, CALL_PHONE)) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(CALL_PHONE),
                    AppConstants.PHONE
                )
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(CALL_PHONE),
                    AppConstants.PHONE
                )
            }
        } else {
            // Permission has already been granted
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            AppConstants.PHONE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted
                    call(phoneNumber)
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                //ignore all requests
            }
        }
    }
}
