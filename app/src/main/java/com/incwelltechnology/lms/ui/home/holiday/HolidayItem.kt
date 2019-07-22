package com.incwelltechnology.lms.ui.home.holiday

import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Holiday
import com.incwelltechnology.lms.databinding.ItemPublicHolidayBinding
import com.xwray.groupie.databinding.BindableItem

class HolidayItem(private val holiday: Holiday) : BindableItem<ItemPublicHolidayBinding>() {
    override fun getLayout(): Int = R.layout.item_public_holiday

    override fun bind(viewBinding: ItemPublicHolidayBinding, position: Int) {
        viewBinding.holiday=holiday
//        setImageViewResource(viewBinding.imageHoliday, holiday.image)
    }

}
//
//@BindingAdapter("android:src")
//fun setImageViewResource(imageView: CircularImageView, uri: String) {
//    Log.d("calli", "$uri and $imageView")
//    return Picasso.get()
//        .load(uri)
//        .error(R.drawable.logo1)
//        .into(imageView)
//}