package ru.xaori.intercomcall.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.pjsip.pjsua2.Endpoint
import ru.xaori.intercomcall.data.pjsip.SipManager
import ru.xaori.intercomcall.presentation.state.CallUIState

class CallViewModel(
    private val sipManager: SipManager,
    private val endpoint: Endpoint,
): ViewModel() {

    private val _state = MutableStateFlow<CallUIState>(CallUIState.Incoming)
    val state: StateFlow<CallUIState> = _state

    init {
        sipManager.onCallStateChanged = { state ->
            _state.value = state
        }
    }

    fun answer() {
        Thread {
            endpoint.libRegisterThread("ExternalThread")
            sipManager.answerCall()
        }.start()

    }
    fun hangup() = sipManager.hangupCall()

}