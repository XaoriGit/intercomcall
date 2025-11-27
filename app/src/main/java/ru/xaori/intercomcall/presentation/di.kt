package ru.xaori.intercomcall.presentation

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.xaori.intercomcall.presentation.viewmodel.CallViewModel
import ru.xaori.intercomcall.presentation.viewmodel.SipViewModel

val presentationModule = module {
    viewModelOf(::SipViewModel)
    viewModelOf(::CallViewModel)
}
