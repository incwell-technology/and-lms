package com.incwelltechnology.lms.ui.home.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.ui.auth.AuthViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {
    private lateinit var birthDate: String
    private lateinit var joinedDate: String
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
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
        Picasso.get()
            .load(authViewModel.user!!.image)
            .placeholder(R.drawable.logo1)
            .error(R.drawable.logo1)
            .into(user_image)
    }

    override fun onResume() {
        super.onResume()
        val navView:NavigationView=activity!!.findViewById(R.id.nav_view)
        navView.menu.getItem(1).isChecked=true
    }

    //convert string of format "yyyy-MM-dd" to date of format "Jul 8, 2019"
    private fun dateFormatter(date:String):String{
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(formatter!!)
    }
}
