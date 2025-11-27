package ru.xaori.intercomcall


import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ru.xaori.intercomcall.data.service.CallVibrationService
import ru.xaori.intercomcall.data.service.fcm.MyFirebaseMessagingService
import ru.xaori.intercomcall.presentation.screen.call.CallScreen
import ru.xaori.intercomcall.presentation.theme.IntercomcallTheme
import kotlin.jvm.java

class IncomingCallActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val nm = getSystemService(NotificationManager::class.java)
        nm.cancel(9999)

        setContent {
            IntercomcallTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CallScreen(intent)
                }
            }
        }
    }
}

