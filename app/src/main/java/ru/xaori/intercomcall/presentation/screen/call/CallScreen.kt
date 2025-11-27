package ru.xaori.intercomcall.presentation.screen.call

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.koinViewModel
import ru.xaori.intercomcall.data.service.fcm.CallActionReceiver
import ru.xaori.intercomcall.presentation.screen.call.component.CallAnswer
import ru.xaori.intercomcall.presentation.screen.call.component.CallEffect
import ru.xaori.intercomcall.presentation.state.CallUIState
import ru.xaori.intercomcall.presentation.viewmodel.CallViewModel

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun CallScreen(intent: Intent, viewModel: CallViewModel = koinViewModel()) {

    val activity = LocalContext.current as? Activity
    val uiState by viewModel.state.collectAsState()

    val declineIntent = Intent(activity, CallActionReceiver::class.java).apply {
        action = "ACTION_DECLINE"
    }
    val declinePending = PendingIntent.getBroadcast(
        activity,
        2,
        declineIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val caller = intent.getStringExtra("caller") ?: "Unknown"
    val isAnswered = intent.getBooleanExtra("callAnswered", false)
    if (isAnswered) viewModel.onCallChange(CallUIState.Answer)


    AnimatedContent(uiState) {
        when (uiState) {
            CallUIState.Answer -> CallAnswer(caller) {
                activity?.finish()
            }

            CallUIState.Call -> CallEffect(
                caller,
                { viewModel.onCallChange(CallUIState.Answer) },
                onDecline = {
                    viewModel.onCallChange(CallUIState.Decline)
                    activity?.finish()
                })

            CallUIState.Decline -> declinePending.send()
        }
    }
}


