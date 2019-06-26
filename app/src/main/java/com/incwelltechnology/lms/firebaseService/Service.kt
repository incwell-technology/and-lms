package com.incwelltechnology.lms.firebaseService

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class Service: FirebaseMessagingService() {
    val tag: String = Service::class.java.simpleName
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Log.d(tag, "From: ${remoteMessage?.from}")

        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(tag, "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob()
            } else {
                // Handle message within 10 seconds
                //handleNow()
            }
        }

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d(tag, "Message Notification Body: ${it.body}")
        }

    }
}