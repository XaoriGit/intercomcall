package ru.xaori.intercomcall.data.service.pjsip

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import ru.xaori.intercomcall.data.pjsip.SipManager

class SipServiceController(
    private val context: Context,
    private val sipManager: SipManager
) {
    fun startService() {
        val intent = Intent(context, SipService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    fun stopService() {
        val intent = Intent(context, SipService::class.java)
        context.stopService(intent)
    }
}
