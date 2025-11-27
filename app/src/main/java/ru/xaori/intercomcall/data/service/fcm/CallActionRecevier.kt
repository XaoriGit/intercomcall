package ru.xaori.intercomcall.data.service.fcm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import ru.xaori.intercomcall.data.service.CallVibrationService

class CallActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "ACTION_DECLINE", "ACTION_DISMISS" -> {
                val nm =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.cancel(9999)
            }
        }
    }
}