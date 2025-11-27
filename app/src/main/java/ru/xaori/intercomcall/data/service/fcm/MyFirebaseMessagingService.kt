package ru.xaori.intercomcall.data.service.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Person
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.xaori.intercomcall.IncomingCallActivity
import ru.xaori.intercomcall.R
import kotlin.jvm.java

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val caller = message.data["caller"] ?: "Unknown"
        showIncomingCallNotification(caller)
    }

    @SuppressLint("FullScreenIntentPolicy")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun showIncomingCallNotification(caller: String) {


        val channelId = "incoming_call_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Incoming Calls",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Incoming call alerts"
            enableVibration(false)
            vibrationPattern = longArrayOf(0)
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE),
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build()
            )
        }
        notificationManager.createNotificationChannel(channel)

        val fullScreenIntent = Intent(this, IncomingCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("caller", caller)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this,
            11,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val answerIntent = Intent(this, IncomingCallActivity::class.java).apply {
            action = "ACTION_ANSWER"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("caller", caller)
            putExtra("callAnswered", true)
        }

        val answerPending = PendingIntent.getActivity(
            this, 1, answerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val declineIntent = Intent(this, CallActionReceiver::class.java).apply {
            action = "ACTION_DECLINE"
        }
        val declinePending = PendingIntent.getBroadcast(
            this, 2, declineIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val incomingCaller = Person.Builder()
            .setName(caller)
            .setImportant(true)
            .build()

        val notification = Notification.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_call)
            .setStyle(Notification.CallStyle.forIncomingCall(incomingCaller, declinePending, answerPending))
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setOngoing(true)
            .setAutoCancel(false)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()

        notificationManager.notify(9999, notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startActivity(fullScreenIntent)
        }
    }
}
