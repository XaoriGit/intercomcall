package ru.xaori.intercomcall.presentation.state

sealed class CallUIState {
    object Idle: CallUIState()
    object Incoming: CallUIState()
    object InCall: CallUIState()
}