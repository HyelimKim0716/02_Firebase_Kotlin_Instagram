package com.example.myapplication.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.view.MainActivity
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by HyelimKim on 2017-12-16.
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "MessagingService"

    /**
     * Called when message is recieved.
     *
     * @param message Object representing the message received from Firebase Cloud Messaging.
     * */
    override fun onMessageReceived(message: RemoteMessage?) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        Log.d(TAG, "From : ${message?.from}")

        val icon = message?.notification?.icon
        val title = message?.notification?.title
        val text = message?.notification?.body
        val sound = message?.notification?.sound

        val obj = message?.data?.get("id")
        var id = obj?.toInt()

        // Check if message contains a data payload.
        if (message?.data?.isNotEmpty()!!) {
            Log.d(TAG, "Message data payload: ${message.data}")

            handleNow()
            /*
            * Check if data needs to processed by long running job
            * For long-running tasks (10 seconds or more) useFirebase Job Dispatcher.
            * */
            // Handle message within 10 seconds

            sendNotification(message.notification)
        }


        Log.d(TAG, "Mesage Notification Body: ${message.notification?.body}")


        /*
        * Also if you intend on generating your own notifications as a result of a received FCM message,
        * here is where that should be initiated. See sendNotification method below.
        * */
    }

    /**
     * Called if getting message is failed.
     * */
    override fun onDeletedMessages() {

    }

    /**
     * Schedule a job using FirebaseJobDispatcher.
     * */
    fun scheduleJob() {
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val job = dispatcher.newJobBuilder()
                .setService(CustomJobService::class.java)
                .setTag("custom-job-tag")
                .build()
        dispatcher.schedule(job)
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     * */
    fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received
     * */
    fun sendNotification(notification: RemoteMessage.Notification?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("TEXT", notification?.body)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                .setContentTitle(notification?.title)
                .setContentText(notification?.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

    }
}