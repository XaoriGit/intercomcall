package ru.xaori.intercomcall.data.pjsip

import android.content.Context
import android.util.Log
import org.pjsip.pjsua2.AccountConfig
import org.pjsip.pjsua2.AuthCredInfo
import org.pjsip.pjsua2.CallOpParam
import org.pjsip.pjsua2.CallSendDtmfParam
import org.pjsip.pjsua2.Endpoint
import org.pjsip.pjsua2.EpConfig
import org.pjsip.pjsua2.LogEntry
import org.pjsip.pjsua2.LogWriter
import org.pjsip.pjsua2.TransportConfig
import org.pjsip.pjsua2.pj_constants_.PJ_TRUE
import org.pjsip.pjsua2.pjsip_inv_state
import org.pjsip.pjsua2.pjsip_status_code
import org.pjsip.pjsua2.pjsip_transport_type_e
import ru.xaori.intercomcall.presentation.state.CallUIState

class SipManager(
    private val appContext: Context, private val ep: Endpoint
) {
    var currentCall: MyCall? = null
        private set

    private var logWriter: LogWriter? = null

    var onCallStateChanged: ((CallUIState) -> Unit)? = null

    private var account: MyAccount? = null


    init {
        try {
            ep.apply {
                libCreate()

                val epConfig = EpConfig()

                logWriter = object : LogWriter() {
                    override fun write(entry: LogEntry) {
                        Log.d("PJSIP", entry.msg)
                    }
                }

                epConfig.logConfig.apply {
                    level = 5
                    consoleLevel = 5
                    decor = 0
                    writer = logWriter
                }

                epConfig.uaConfig.apply {
                    stunServer.add("stun.linphone.org:3478")
                }

                epConfig.medConfig.apply {
                    audioFramePtime = 40
                }

                libInit(epConfig)

                val tpCfg = TransportConfig()
                transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, tpCfg)

                libStart()
            }


            val cfg = AccountConfig().apply {
                idUri = "sip:6002@217.25.212.61"
                regConfig.registrarUri = "sip:217.25.212.61"
                sipConfig.authCreds.add(
                    AuthCredInfo(
                        "digest", "*", "6002", 0, "6002"
                    )
                )

                natConfig.apply {
                    iceEnabled = true
                    turnEnabled = false
                    contactRewriteUse = PJ_TRUE
                }
            }
            account = MyAccount(ep, cfg) { call ->
                onIncomingCall(call)
            }

            Log.d("PJSIP", "PJSIP был иннициализирован")
        } catch (e: Exception) {
            Log.e("PJSIP", "Ошибка инициализации PJSIP: ${e.message}")
        }
    }


    fun onIncomingCall(call: MyCall) {
        currentCall = call

        call.onStateChanged = { state ->
            when (state) {
                pjsip_inv_state.PJSIP_INV_STATE_INCOMING -> onCallStateChanged?.invoke(CallUIState.Incoming)
                pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED,
                pjsip_inv_state.PJSIP_INV_STATE_EARLY -> onCallStateChanged?.invoke(CallUIState.InCall)
                pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED,
                pjsip_inv_state.PJSIP_INV_STATE_NULL -> {
                    currentCall = null
                    onCallStateChanged?.invoke(CallUIState.Idle)
                }
            }
            Log.d("CALL", state.toString())
        }
    }

    fun answerCall() {
        try {
            currentCall?.let { call ->
                val prm = CallOpParam()
                prm.statusCode = 200
                call.answer(prm)
            }
        } catch (e: Exception) {
            Log.e("PJSIP", "Answer failed: ${e.message}")
        }
        onCallStateChanged?.invoke(CallUIState.InCall)
    }


    fun hangupCall() {
        Log.d("CALL", "Положили трубку")
        currentCall?.let { call ->
            try {
                val prm = CallOpParam()

                if (call.info.state == pjsip_inv_state.PJSIP_INV_STATE_INCOMING || call.info.state == pjsip_inv_state.PJSIP_INV_STATE_EARLY) {

                    prm.statusCode = pjsip_status_code.PJSIP_SC_DECLINE
                    call.hangup(prm)
                } else {
                    call.hangup(CallOpParam())
                }

            } catch (e: Exception) {
                Log.e("PJSIP", "Ошибка при завершении/отклонении: ${e.message}")
            } finally {
                currentCall = null
            }
        }
        onCallStateChanged?.invoke(CallUIState.Idle)
    }

    fun openDoor() {
        currentCall?.let { call ->
            if (call.info.state == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED || call.info.state == pjsip_inv_state.PJSIP_INV_STATE_NULL) {
                onCallStateChanged?.invoke(CallUIState.Idle)
                return
            }

            call.sendDtmf(
                CallSendDtmfParam().apply {
                    digits = "1"
                })
        }
    }
}