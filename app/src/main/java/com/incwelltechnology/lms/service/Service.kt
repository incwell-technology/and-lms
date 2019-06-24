package com.incwelltechnology.lms.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class Service: FirebaseMessagingService() {
    private val tag: String = this.javaClass.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        // Check if message contains a data payload.
        if (remoteMessage!!.data.isNotEmpty()) {
            Log.d(tag, "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob()
            } else {
                // Handle message within 10 seconds
                //handleNow()
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(tag, "Message Notification Body: " + remoteMessage.notification!!.body)
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d(tag, "Refreshed token: $token")

    }
}