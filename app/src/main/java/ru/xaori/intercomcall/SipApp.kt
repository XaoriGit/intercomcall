package ru.xaori.intercomcall

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
class SipApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SipApp)
            modules(commonModule)
        }
    }
}