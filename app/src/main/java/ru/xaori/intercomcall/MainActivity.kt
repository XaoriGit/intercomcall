package ru.xaori.intercomcall

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.google.firebase.messaging.FirebaseMessaging
import ru.xaori.intercomcall.presentation.screen.SipScreen
import ru.xaori.intercomcall.data.service.SipForegroundService
import ru.xaori.intercomcall.presentation.screen.call.CallScreen
import ru.xaori.intercomcall.presentation.theme.IntercomcallTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS
        )
        ActivityCompat.requestPermissions(this@MainActivity, permissions, 0)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()

        FirebaseMessaging.getInstance().subscribeToTopic("push")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to topic successfully")
                } else {
                    Log.d("FCM", "Failed to subscribe to topic")
                }
            }

//        SipForegroundService.startService(
//            context = this,
//            accountUri = "sip:6002@217.25.212.62",
//            registrarUri = "sip:217.25.212.62",
//            username = "6002",
//            password = "6002"
//        )
//        SipForegroundService.startService(
//            context = this,
//            accountUri = "sips:xaori3@sip.linphone.org",
//            registrarUri = "sips:sip.linphone.org",
//            username = "xaori3",
//            password = "cbplUQ!QUVMRg3r"
//        )

        setContent {
            IntercomcallTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Пустота")
                    }
                }
            }
        }
    }
}
