package ru.xaori.intercomcall

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import ru.xaori.intercomcall.ui.theme.IntercomcallTheme


// Account ID
const val ACC_DOMAIN = "192.168.88.114"
const val ACC_USER   = "101060"
const val ACC_ID_URI = "Kotlin <sip:$ACC_USER@$ACC_DOMAIN>"

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            1
        )


        setContent {
            IntercomcallTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {

                    }) {
                        Text("Register SIP")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {

                    }) {
                        Text("Call SIP")
                    }
                }
            }
        }
    }
}