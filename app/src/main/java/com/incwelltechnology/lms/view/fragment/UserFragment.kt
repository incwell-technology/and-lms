package com.incwelltechnology.lms.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.Profile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class UserFragment : Fragment() {
    private lateinit var birthDate: String
    private lateinit var joinedDate: String

    companion object {
        fun getInstance(user: Profile): UserFragment {
            val userFragment = UserFragment()
            val bundle = Bundle()
            bundle.putParcelable("user", user)
            userFragment.arguments = bundle
            return userFragment
        }
    }

    //    @SuppressLint("SimpleDateFormat")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = arguments!!.getParcelable<Profile>("user")

        birthDate = user!!.date_of_birth
        joinedDate = user.joined_date

        annual_leave.progress = "${user.annual_leaves}".toFloat()
        compensation_leave.progress = "${user.compensation_leaves}".toFloat()
        sick_leave.progress = "${user.sick_leaves}".toFloat()
        user_name.text = user.full_name
        phone_number.text = user.phone_number
        email.text = user.email
        date_of_birth.text = dateFormatter(birthDate)
        joined_date.text = dateFormatter(joinedDate)
        leave_issuer.text = user.leave_issuer
        Picasso.get()
            .load(user.image)
            .placeholder(R.drawable.dummy)
            .error(R.drawable.dummy)
            .into(user_image)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    //convert string of format "yyyy-MM-dd" to date of format "Jul 8, 2019"
    private fun dateFormatter(date:String):String{
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(formatter)
    }
}
