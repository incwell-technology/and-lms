package com.incwelltechnology.lms.ui.employee

import android.Manifest
import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
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
    private lateinit var messageIcon: Drawable
    private lateinit var callIcon: Drawable
    private var swipeBackgroundColorRight: ColorDrawable = ColorDrawable(Color.parseColor("#0099ff"))
    private var swipeBackgroundColorLeft: ColorDrawable = ColorDrawable(Color.parseColor("#0099ff"))
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
            val adapter = EmployeeAdapter(employee)
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
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    CALL_PHONE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    AppConstants.PHONE
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
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
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
