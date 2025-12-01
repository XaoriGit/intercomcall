package ru.xaori.intercomcall.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.xaori.intercomcall.data.pjsip.SipManager
import ru.xaori.intercomcall.presentation.state.CallUIState

class SipViewModel(
    private val sipManager: SipManager
): ViewModel() {

    private val _state = MutableStateFlow<CallUIState>(CallUIState.Idle)
    val state: StateFlow<CallUIState> = _state

    init {
        sipManager.onCallStateChanged = { state ->
            _state.value = state
        }
    }

    fun answer() {
        sipManager.answerCall()
    }
    fun hangup() = sipManager.hangupCall()

    fun openDoor() = sipManager.openDoor()
}
