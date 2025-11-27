package ru.xaori.intercomcall.presentation.screen.call.component

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun CallEffect(
    title: String,
    onAnswer: () -> Unit = {},
    onDecline: () -> Unit = {}
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    val boxSize = 80.dp
    val initialColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val animatedColor = remember { Animatable(initialColor) }
    val animatedOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val maxDrag = 300f
    val bounceOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            bounceOffset.animateTo(-20f, animationSpec = tween(300))
            bounceOffset.animateTo(0f, animationSpec = tween(300))

            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))

            delay(1000)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            title,
            style = MaterialTheme.typography.displaySmall
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset {
                    IntOffset(
                        0,
                        (animatedOffset.value + bounceOffset.value).roundToInt()
                    )
                }
                .size(boxSize)
                .shadow(8.dp, CircleShape, spotColor = MaterialTheme.colorScheme.primary)
                .border(
                    0.5.dp,
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    CircleShape
                )
                .background(animatedColor.value, CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            when {
                                animatedOffset.value < -maxDrag / 2 -> onAnswer()
                                animatedOffset.value > maxDrag / 2 -> onDecline()
                            }
                            scope.launch {
                                animatedOffset.animateTo(0f, animationSpec = tween(300))
                                animatedColor.animateTo(initialColor, animationSpec = tween(300))
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        val newOffset = (animatedOffset.value + dragAmount.y).coerceIn(-maxDrag, maxDrag)
                        scope.launch { animatedOffset.snapTo(newOffset) }

                        val fraction = (newOffset / maxDrag).coerceIn(-1f, 1f)
                        val targetColor = when {
                            fraction < 0 -> lerp(initialColor, Color(0xFF4CAF50), -fraction)
                            fraction > 0 -> lerp(initialColor, Color(0xFFF44336), fraction)
                            else -> initialColor
                        }
                        scope.launch { animatedColor.snapTo(targetColor) }
                    }
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Call,
                contentDescription = "call",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }
    }
}


fun lerp(start: Color, end: Color, fraction: Float): Color {
    return Color(
        red = start.red + (end.red - start.red) * fraction,
        green = start.green + (end.green - start.green) * fraction,
        blue = start.blue + (end.blue - start.blue) * fraction,
        alpha = start.alpha + (end.alpha - start.alpha) * fraction
    )
}