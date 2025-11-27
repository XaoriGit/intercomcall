package ru.xaori.intercomcall.presentation.screen.call.component

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.widget.DialogTitle
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CallAnswer(
    title: String,
    onDecline: () -> Unit
) {
    var time by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while(true) {
            kotlinx.coroutines.delay(1000)
            time++
        }
    }

    val minutes = time / 60
    val seconds = time % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                title,
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                timeFormatted,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .shadow(8.dp, CircleShape, spotColor = MaterialTheme.colorScheme.primary)
                .border(
                    0.5.dp,
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    CircleShape
                )
                .background(Color(0xFFF44336), CircleShape)
                .clickable {
                    onDecline()
                }
        ) {
            Icon(
                imageVector = Icons.Filled.CallEnd,
                contentDescription = "call",
                tint = Color.White,
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }
    }
}
