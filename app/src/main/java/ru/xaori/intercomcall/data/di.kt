package ru.xaori.intercomcall.data

import org.koin.dsl.module
import ru.xaori.intercomcall.data.repository.SipManager

val dataModule = module {
    single { SipManager() }
}
