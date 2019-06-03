package com.incwelltechnology.lms.activity.profileActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.model.UserProfile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {

    val TAG = UserFragment.javaClass.simpleName
    companion object {
//        var count = 0
        fun getInstance(userProfile: UserProfile): UserFragment {
            val userFragment = UserFragment()
            val bundle = Bundle()
            bundle.putParcelable("user", userProfile)
            userFragment.arguments = bundle
            return userFragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user = arguments!!.getParcelable<UserProfile>("user")
        full_name.text=user.full_name
        department.text=user.department
        annual_leave.text=user.annual_leave.toString()
        compensation_leaves.text=user.compensation_leaves.toString()
        sick_leave.text=user.sick_leave.toString()
        user_name.text=user.full_name
        phone_number.text=user.phone_number
        email.text=user.email
        date_of_birth.text=user.date_of_birth
        joined_date.text=user.joined_date
        leave_issuer.text="Leave Issuer: ${user.leave_issuer}"
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
}
