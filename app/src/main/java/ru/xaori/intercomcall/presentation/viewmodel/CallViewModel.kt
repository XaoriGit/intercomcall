package ru.xaori.intercomcall.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.xaori.intercomcall.presentation.state.CallUIState

class CallViewModel: ViewModel() {
    private val _state = MutableStateFlow<CallUIState>(CallUIState.Call)
    val state: StateFlow<CallUIState> = _state

    fun onCallChange(value: CallUIState) {
        _state.value = value
    }
}