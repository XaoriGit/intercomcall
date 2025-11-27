package ru.xaori.intercomcall.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SipViewModel(): ViewModel() {

    var callState by mutableStateOf("Idle")
        private set
}
