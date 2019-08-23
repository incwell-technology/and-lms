package com.incwelltechnology.lms.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.incwelltechnology.lms.R

fun Context.toast(message:String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun View.snack(message: String){
    Snackbar.make(this,message,Snackbar.LENGTH_LONG).setAction("",null).show()
}

fun ProgressBar.show(){
    max=100
    isIndeterminate=true
    visibility= View.VISIBLE
}

fun ProgressBar.hide(){
    visibility= View.GONE
}

//eliminating error hint when editext is started to fill
fun hideErrorHint(textInputEditText: TextInputEditText,textInputLayout: TextInputLayout){
    textInputEditText.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textInputLayout.error = null
        }
    })
}

fun hideErrorHintAutoCompleteTextView(autoCompleteTextView: AutoCompleteTextView,textInputLayout: TextInputLayout){
    autoCompleteTextView.addTextChangedListener(object : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            textInputLayout.error = null
        }

    })
}

fun dropDown(context: Context, objects: Array<String>, id: AutoCompleteTextView) {
    id.setAdapter(ArrayAdapter(context, R.layout.dropdown_menu_popup_item, objects))
}