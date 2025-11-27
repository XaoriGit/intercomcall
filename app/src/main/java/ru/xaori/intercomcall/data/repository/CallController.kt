package ru.xaori.intercomcall.data.repository

import org.pjsip.pjsua2.CallOpParam
import org.pjsip.pjsua2.pjsip_status_code
import ru.xaori.intercomcall.data.pjsip.MyCall

class CallController {
    var currentCall: MyCall? = null

    fun setIncomingCall(call: MyCall) {
        currentCall = call
    }

    fun answer() {
        currentCall?.let {
            val prm = CallOpParam().apply {
                statusCode = pjsip_status_code.PJSIP_SC_OK
            }
            it.answer(prm)
        }
    }

    fun hangup() {
        currentCall?.hangup(CallOpParam())
    }
}