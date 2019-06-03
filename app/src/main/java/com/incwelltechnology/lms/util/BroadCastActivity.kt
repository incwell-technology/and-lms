package com.incwelltechnology.lms.util

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.incwelltechnology.lms.R


class BroadCastActivity:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(!AppUtil.isNetworkConnected(context!!)){
            openDialog(context)
            Toast.makeText(context, "check your internet connection", Toast.LENGTH_LONG).show()
        }
    }
    private fun openDialog(context: Context?) {
        val dialog = Dialog(context!!) // Context, this, etc.
        dialog.setContentView(R.layout.alert_dialog)
        dialog.setTitle("No internet connectivity")
        dialog.show()
    }
}