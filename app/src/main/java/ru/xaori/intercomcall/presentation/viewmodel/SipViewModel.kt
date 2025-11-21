package ru.xaori.intercomcall.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.xaori.intercomcall.data.repository.SipManager

class SipViewModel(private val sipManager: SipManager): ViewModel() {

    var callState by mutableStateOf("Idle")
        private set

    fun initSip() {
        sipManager.init(
            "sip:101060@217.25.212.61",
            "sip:217.25.212.61",
            "6001",
            "6001"
        )
    }

    fun call(target: String) {
        sipManager.makeCall(target)
        callState = "Calling $target"
    }

    fun destroy() {
        sipManager.destroy()
    }
}
