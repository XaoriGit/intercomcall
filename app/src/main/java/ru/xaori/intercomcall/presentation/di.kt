package ru.xaori.intercomcall.presentation

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.xaori.intercomcall.presentation.viewmodel.CallViewModel
import ru.xaori.intercomcall.presentation.viewmodel.SipViewModel

val presentationModule = module {
    viewModelOf(::CallViewModel)
    viewModelOf(::SipViewModel)
}
