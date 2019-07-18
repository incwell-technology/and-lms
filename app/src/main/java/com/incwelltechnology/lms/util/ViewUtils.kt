package com.incwelltechnology.lms.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

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
