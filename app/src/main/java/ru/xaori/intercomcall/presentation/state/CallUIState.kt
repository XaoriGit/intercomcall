package ru.xaori.intercomcall.presentation.state

sealed class CallUIState {
    object Call: CallUIState()
    object Answer: CallUIState()
    object Decline: CallUIState()
}