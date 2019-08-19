package com.incwelltechnology.lms.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.incwelltechnology.lms.AppConstants
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.util.Coroutine
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent

class FirebaseService : FirebaseMessagingService() {
    private val tag: String = FirebaseService::class.java.simpleName
    var state=false
    private val channelId: String = "incwell_lms"
    private val channelName: String = "Incwell LMS"
    private val channelDesc: String = "Incwell Notification Service"

    private lateinit var broadcastManager: LocalBroadcastManager

    override fun onCreate() {
        super.onCreate()
        broadcastManager= LocalBroadcastManager.getInstance(this)

    }
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
            val textTitle: String = it.title!!
            val textBody: String = it.body!!
            Coroutine.io {
                showNotification(applicationContext, textTitle, textBody)
            }
            state = true
            Hawk.put(AppConstants.NOTIFICATION_KEY,state)
            val intent=Intent(AppConstants.NOTIFICATION_STATE)
            intent.putExtra(AppConstants.key,true)
            broadcastManager.sendBroadcast(intent)
        }
    }
    override fun onNewToken(token: String?) {
        Log.d(tag, "Refreshed token: " + token!!)

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDesc
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context, textTitle: String, textContent: String) {

        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo1)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(1, notificationBuilder.build())
    }
}