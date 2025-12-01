package ru.xaori.intercomcall.data.pjsip

import android.util.Log
import org.pjsip.pjsua2.Account
import org.pjsip.pjsua2.AccountConfig
import org.pjsip.pjsua2.Endpoint
import org.pjsip.pjsua2.OnIncomingCallParam
import org.pjsip.pjsua2.OnRegStateParam
import org.pjsip.pjsua2.pjsip_status_code

class MyAccount(
    private val endpoint: Endpoint,
    cfg: AccountConfig,
    private val onIncomingCallCallback: (MyCall) -> Unit
) : Account() {
    init {
        create(cfg)
    }

    override fun onIncomingCall(prm: OnIncomingCallParam) {
        val call = MyCall(endpoint, this, prm.callId)
        onIncomingCallCallback(call)
    }

    override fun onRegState(prm: OnRegStateParam?) {
        val active = prm?.code == pjsip_status_code.PJSIP_SC_OK
        Log.d("PJSIP", "Регистрация: ${if (active) "успешна" else "неудачна"}")
    }
}
