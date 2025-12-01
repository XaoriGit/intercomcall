package ru.xaori.intercomcall.data.service.fcm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.xaori.intercomcall.data.service.pjsip.SipService

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val caller = message.data["caller"] ?: "Unknown"

        val intent = Intent(this, SipService::class.java).apply {
            action = "INCOMING_CALL"
            putExtra("caller", caller)
        }
        startForegroundService(intent)
    }
}
