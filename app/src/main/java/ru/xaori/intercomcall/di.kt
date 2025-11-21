package ru.xaori.intercomcall

import org.koin.dsl.module
import ru.xaori.intercomcall.data.dataModule
import ru.xaori.intercomcall.presentation.presentationModule

val commonModule = module {
    includes(dataModule)
    includes(presentationModule)

}