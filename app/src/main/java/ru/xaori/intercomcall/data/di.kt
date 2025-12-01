package ru.xaori.intercomcall.data

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.pjsip.pjsua2.Endpoint
import ru.xaori.intercomcall.data.pjsip.SipManager
import ru.xaori.intercomcall.data.service.pjsip.SipServiceController

val dataModule = module {

    singleOf(::Endpoint)

    singleOf(::SipManager)

    singleOf(::SipServiceController)
}
