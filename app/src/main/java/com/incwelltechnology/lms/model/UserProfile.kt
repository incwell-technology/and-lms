package com.incwelltechnology.lms.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class  UserProfile(
    val full_name : String = "",
    val phone_number: String = "",
    var email: String= "",
    val department: String = "",
    var leave_issuer: String ="",
    var image: String = "",
    var sick_leave: Int =0,
    var annual_leave:Int = 0,
    var compensation_leaves: Int =0,
    val date_of_birth:String = "",
    val joined_date:String = "",
    var id:Int=0
) : Parcelable
