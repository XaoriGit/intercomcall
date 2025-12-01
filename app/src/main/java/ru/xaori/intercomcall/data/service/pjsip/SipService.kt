package ru.xaori.intercomcall.data.service.pjsip

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Person
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.koin.android.ext.android.inject
import ru.xaori.intercomcall.IncomingCallActivity
import ru.xaori.intercomcall.R
import ru.xaori.intercomcall.data.pjsip.SipManager
import ru.xaori.intercomcall.presentation.state.CallUIState

class SipService : Service() {

    val sipManager: SipManager by inject()

    private var currentIncomingNotificationId: Int = 0


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate() {
        super.onCreate()

        startForeground(1, createBaseNotification())

        sipManager.onCallStateChanged = { state ->
            Log.d("CALL", "$state")
            when (state) {
                CallUIState.Incoming -> {
                    showIncomingCallNotification("Unknown")
                }

                CallUIState.InCall -> {
                    updateNotification(createInCallNotification())
                }

                CallUIState.Idle -> {
                    updateNotification(createBaseNotification())
                }
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            "ACTION_ANSWER" -> {
                sipManager.answerCall()
            }
            "ACTION_DECLINE" -> {
                cancelIncomingCallNotification()
                sipManager.hangupCall()
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun updateNotification(notification: Notification) {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(1, notification)
    }
    private fun createBaseNotification(): Notification {
        val channelId = "sip_base"

        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                channelId, "SIP Service", NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("SIP Service Running")
            .setSmallIcon(R.drawable.ic_call)
            .setOngoing(true)
            .build()
    }

    private fun createInCallNotification(): Notification {
        val channelId = "sip_incall"

        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                channelId, "In Call", NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("In Call")
            .setSmallIcon(R.drawable.ic_call)
            .setOngoing(true)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission", "FullScreenIntentPolicy")
    private fun showIncomingCallNotification(caller: String) {

        val channelId = "incoming_sip"

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

        val fullScreenIntent = Intent(this, IncomingCallActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("caller", caller)
            }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, 100, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val answerIntent = Intent(this, SipService::class.java).apply {
            action = "ACTION_ANSWER"
        }

        val answerPending = PendingIntent.getService(
            this, 10, answerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val declineIntent = Intent(this, SipService::class.java).apply {
            action = "ACTION_DECLINE"
        }

        val declinePending = PendingIntent.getService(
            this, 11, declineIntent,
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
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()

        val id = System.currentTimeMillis().toInt()

        currentIncomingNotificationId = id

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(id, notification)

        startActivity(fullScreenIntent)
    }

    private fun cancelIncomingCallNotification() {
        val nm = getSystemService(NotificationManager::class.java)
        nm.cancel(currentIncomingNotificationId)
    }
}
