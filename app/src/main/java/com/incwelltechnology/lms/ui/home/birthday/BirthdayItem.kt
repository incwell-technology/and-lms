package com.incwelltechnology.lms.ui.home.birthday

import android.util.Log
import androidx.databinding.BindingAdapter
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.data.model.Birthday
import com.incwelltechnology.lms.databinding.ItemUserBirthdayBinding
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem

class BirthdayItem(private val birthday: Birthday) : BindableItem<ItemUserBirthdayBinding>() {
    override fun getLayout(): Int = R.layout.item_user_birthday

    override fun bind(viewBinding: ItemUserBirthdayBinding, position: Int) {
        viewBinding.birthday = birthday
        setImageViewResource(viewBinding.imageBirthday, birthday.image)
    }
}

@BindingAdapter("android:src")
fun setImageViewResource(imageView: CircularImageView, uri: String) {
    Log.d("calli", "$uri and $imageView")
    return Picasso.get()
        .load(uri)
        .error(R.drawable.logo1)
        .into(imageView)
}
