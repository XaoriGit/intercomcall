package ru.xaori.intercomcall.data.pjsip

import android.content.Context
import android.util.Log
import org.pjsip.pjsua2.Account
import org.pjsip.pjsua2.Endpoint
import org.pjsip.pjsua2.OnIncomingCallParam
import ru.xaori.intercomcall.data.repository.CallController
import ru.xaori.intercomcall.data.service.SipForegroundService

class MyAccount(
    private val ep: Endpoint,
    private val callController: CallController,
    private val serviceContext: Context?
) : Account() {
    override fun onIncomingCall(prm: OnIncomingCallParam) {
        Log.d("SIP", "INVITE INVITE INVITE INVITE INVITE")
        val call = MyCall(this, prm.callId, ep)

        callController.setIncomingCall(call)

        (serviceContext as SipForegroundService).showIncomingCallNotification()
    }
}