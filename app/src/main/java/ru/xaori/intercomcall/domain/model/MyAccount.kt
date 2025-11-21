package ru.xaori.intercomcall.domain.model

import org.pjsip.pjsua2.Account
import org.pjsip.pjsua2.CallOpParam
import org.pjsip.pjsua2.Endpoint
import org.pjsip.pjsua2.OnIncomingCallParam
import org.pjsip.pjsua2.OnRegStateParam
import org.pjsip.pjsua2.pjsip_status_code

class MyAccount(private val ep: Endpoint) : Account() {

    override fun onRegState(prm: OnRegStateParam) {
        super.onRegState(prm)
        println("*** Registration state: ${prm.code} ${prm.reason}")
    }

    override fun onIncomingCall(prm: OnIncomingCallParam) {
        val call = MyCall(this, prm.callId, ep) // передаем ep
        val callPrm = CallOpParam()
        callPrm.statusCode = pjsip_status_code.PJSIP_SC_OK
        call.answer(callPrm) // Авто-принятие звонка
    }
}