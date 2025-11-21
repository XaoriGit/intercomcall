package ru.xaori.intercomcall.data.repository

import org.pjsip.pjsua2.AccountConfig
import org.pjsip.pjsua2.AuthCredInfo
import org.pjsip.pjsua2.CallOpParam
import org.pjsip.pjsua2.Endpoint
import org.pjsip.pjsua2.EpConfig
import org.pjsip.pjsua2.TransportConfig
import org.pjsip.pjsua2.pjsip_transport_type_e
import ru.xaori.intercomcall.domain.model.MyAccount
import ru.xaori.intercomcall.domain.model.MyCall

class SipManager {

    private var ep: Endpoint? = null
    private var acc: MyAccount? = null

    init {
        System.loadLibrary("pjsua2")
    }

    fun init(accountUri: String, registrarUri: String, username: String, password: String) {
        ep = Endpoint().apply {
            libCreate()
            libInit(EpConfig())
            val tpCfg = TransportConfig()
            tpCfg.port = 5060
            transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, tpCfg)
            libStart()
        }

        val cfg = AccountConfig().apply {
            idUri = accountUri
            regConfig.registrarUri = registrarUri
            sipConfig.authCreds.add(AuthCredInfo("digest", "*", username, 0, password))
        }

        acc = MyAccount(ep!!).apply { create(cfg) }
    }

    fun makeCall(targetUri: String) {
        val call = MyCall(acc!!, -1, ep!!)
        val prm = CallOpParam(true)
        call.makeCall(targetUri, prm)
    }

    fun destroy() {
        acc?.delete()
        ep?.libDestroy()
        ep?.delete()
    }

    fun getAccount() = acc
}
