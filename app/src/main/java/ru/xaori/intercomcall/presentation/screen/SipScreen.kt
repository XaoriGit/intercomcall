package ru.xaori.intercomcall.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.xaori.intercomcall.presentation.viewmodel.SipViewModel

@Composable
fun SipScreen(vm: SipViewModel = koinViewModel()) {
    var target by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("State: ${vm.callState}")
    }
}
