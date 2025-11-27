package ru.xaori.intercomcall.data.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import ru.xaori.intercomcall.IncomingCallActivity
import ru.xaori.intercomcall.R

class CallVibrationService : Service() {

    private var vibrator: Vibrator? = null

    companion object {
        private var vibratorStatic: Vibrator? = null

        fun stopVibration() {
            vibratorStatic?.cancel()
        }
    }

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibratorStatic = vibrator
    }

    @SuppressLint("FullScreenIntentPolicy")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val caller = intent?.getStringExtra("caller") ?: "Unknown"

        val channelId = "call_vibration_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Incoming Call VIBRO",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Incoming call vibration"
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 1000, 1000)
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE),
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build()
            )
        }
        notificationManager.createNotificationChannel(channel)

        val fullScreenIntent = Intent(this, IncomingCallActivity::class.java).apply {
            START_FLAG_REDELIVERY or START_FLAG_REDELIVERY
            putExtra("caller", caller)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, 0, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_call)
            .setContentTitle("Incoming call")
            .setContentText(caller)
            .setOngoing(true)
            .setCategory(Notification.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            .setVibrate(longArrayOf(0, 1000, 1000))
            .build()

        startForeground(9990, notification)

        vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 1000, 1000), 0))

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator?.cancel()
        vibratorStatic = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}