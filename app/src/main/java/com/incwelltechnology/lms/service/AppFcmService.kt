package com.incwelltechnology.lms.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppFcmService : FirebaseMessagingService() {
    val TAG: String = AppFcmService::class.java.simpleName
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        Log.d(TAG, "message received" + p0!!.data)
    }
}