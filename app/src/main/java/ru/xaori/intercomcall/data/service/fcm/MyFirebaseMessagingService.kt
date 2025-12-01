package ru.xaori.intercomcall.data.service.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Person
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import ru.xaori.intercomcall.IncomingCallActivity
import ru.xaori.intercomcall.R
import ru.xaori.intercomcall.data.service.pjsip.SipService
import ru.xaori.intercomcall.data.service.pjsip.SipServiceController
import kotlin.jvm.java

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val sipServiceController: SipServiceController by inject()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val caller = message.data["caller"] ?: "Unknown"

        val intent = Intent(this, SipService::class.java).apply {
            action = "INCOMING_CALL"
            putExtra("caller", caller)
        }
        startForegroundService(intent)

//        showIncomingCallNotification(caller)
    }

    @SuppressLint("FullScreenIntentPolicy")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun showIncomingCallNotification(caller: String) {
        val channelId = "incoming_calls"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Incoming Calls",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Incoming call alerts"
            enableVibration(true)
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
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



        val incomingCaller = Person.Builder()
            .setName(caller)
            .setImportant(true)
            .build()

//        val notification = Notification.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_call)
//            .setStyle(Notification.CallStyle.forIncomingCall(incomingCaller, declinePending, answerPending))
//            .setCategory(NotificationCompat.CATEGORY_CALL)
//            .setOngoing(true)
//            .setAutoCancel(false)
//            .setFullScreenIntent(fullScreenPendingIntent, true)
//            .build()
//
//        notificationManager.notify(9999, notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startActivity(fullScreenIntent)
        }
    }
}
