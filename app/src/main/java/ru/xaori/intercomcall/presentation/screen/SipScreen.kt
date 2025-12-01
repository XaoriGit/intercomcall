package ru.xaori.intercomcall.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.xaori.intercomcall.presentation.state.CallUIState
import ru.xaori.intercomcall.presentation.viewmodel.SipViewModel

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun SipScreen(viewModel: SipViewModel = koinViewModel()) {
    val uiState by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(uiState) {
            when (val state = uiState) {
                CallUIState.Idle -> Text("Готов")
                CallUIState.Incoming -> {
                    Row {
                        Button(onClick = { viewModel.answer() }, modifier = Modifier.padding(8.dp)) {
                            Text("Ответить")
                        }
                        Button(onClick = { viewModel.hangup() }, modifier = Modifier.padding(8.dp)) {
                            Text("Отклонить")
                        }
                    }
                }
                CallUIState.InCall -> Row() {
                    Button(onClick = { viewModel.hangup() }) {
                        Text("Завершить")
                    }
                    Button(onClick = { viewModel.openDoor() }) {
                        Text("Открыть дверь")
                    }
                }
            }
        }
    }
}