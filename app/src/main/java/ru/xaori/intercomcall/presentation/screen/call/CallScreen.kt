package ru.xaori.intercomcall.presentation.screen.call

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.koinViewModel
import ru.xaori.intercomcall.presentation.screen.call.component.CallAnswer
import ru.xaori.intercomcall.presentation.screen.call.component.CallEffect
import ru.xaori.intercomcall.presentation.state.CallUIState
import ru.xaori.intercomcall.presentation.viewmodel.CallViewModel

@SuppressLint("UnusedContentLambdaTargetStateParameter", "ContextCastToActivity")
@Composable
fun CallScreen(intent: Intent, viewModel: CallViewModel = koinViewModel()) {
    val activity = LocalContext.current as? Activity
    val uiState by viewModel.state.collectAsState()

    val caller = intent.getStringExtra("caller") ?: "Unknown"


    AnimatedContent(uiState) {
        when (uiState) {
            CallUIState.InCall -> CallAnswer(caller) {
                activity?.finish()
            }

            CallUIState.Incoming -> CallEffect(
                caller,
                { viewModel.answer() },
                onDecline = {
                    viewModel.hangup()
                    activity?.finish()
                })

            CallUIState.Idle -> {}
        }
    }
}


