package com.incwelltechnology.lms.ui.home.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import com.incwelltechnology.lms.ui.home.HomeViewModel
import com.incwelltechnology.lms.util.FileUtils.getRealPathFromURI
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {
    private lateinit var birthDate: String
    private lateinit var joinedDate: String
    private val authViewModel: AuthViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.incwelltechnology.lms.R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.sharedPreference()

        birthDate = authViewModel.user!!.date_of_birth
        joinedDate = authViewModel.user!!.joined_date

        annual_leave.progress = "${authViewModel.user!!.annual_leaves}".toFloat()
        compensation_leave.progress = "${authViewModel.user!!.compensation_leaves}".toFloat()
        sick_leave.progress = "${authViewModel.user!!.sick_leaves}".toFloat()

        user_name.text = authViewModel.user!!.full_name
        phone_number.text = authViewModel.user!!.phone_number
        email.text = authViewModel.user!!.email
        date_of_birth.text = dateFormatter(birthDate)
        joined_date.text = dateFormatter(joinedDate)
        leave_issuer.text = authViewModel.user!!.leave_issuer
        //live data to observe change in profile picture
        homeViewModel.usrProImage.observe(this, androidx.lifecycle.Observer {
            Picasso.get()
                .load(it)
                .placeholder(com.incwelltechnology.lms.R.drawable.logo1)
                .error(com.incwelltechnology.lms.R.drawable.logo1)
                .into(user_image)
        })


        edit_photo.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    //permission denied
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, AppConstants.PERMISSION_CODE)

                } else {
                    //permission already granted
                    changeDp()
                }
            } else {
                changeDp()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val navView: NavigationView = activity!!.findViewById(com.incwelltechnology.lms.R.id.nav_view)
        navView.menu.getItem(1).isChecked = true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            AppConstants.PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted
                    changeDp()
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show()
                }
                return
            }
            else -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == AppConstants.IMAGEPICK_CODE && data != null && data.data != null) {
            val uri = data.data
            val fileActualPath = getRealPathFromURI(context!!, uri!!) //retrieve actual path of data from uri
            val file = File(fileActualPath) //build object from actual path
            // create RequestBody instance from file
            val requestFile = RequestBody.create(
                MediaType.parse(context!!.contentResolver.getType(uri)!!), file
            )
            //Build multipartbody from request body
            val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestFile)
            //call 'uploadProfile()' from homeViewModel and pass necessary parameters
            homeViewModel.uploadProfile(multipartBody, authViewModel.user!!.id)
        }
    }

    //function to change profile picture
    private fun changeDp() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, AppConstants.IMAGEPICK_CODE)
    }

    //convert string of format "yyyy-MM-dd" to date of format "Jul 8, 2019"
    private fun dateFormatter(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(formatter!!)
    }
}
