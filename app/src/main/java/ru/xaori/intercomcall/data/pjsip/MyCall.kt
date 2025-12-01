package ru.xaori.intercomcall.data.pjsip

import android.util.Log
import org.pjsip.pjsua2.Account
import org.pjsip.pjsua2.Call
import org.pjsip.pjsua2.Endpoint
import org.pjsip.pjsua2.OnCallMediaStateParam
import org.pjsip.pjsua2.OnCallStateParam
import org.pjsip.pjsua2.pjmedia_type
import org.pjsip.pjsua2.pjsua_call_media_status

class MyCall(private val endpoint: Endpoint, acc: Account, callId: Int = -1) : Call(acc, callId) {

    var onStateChanged: ((Int) -> Unit)? = null

    override fun onCallState(prm: OnCallStateParam?) {
        Log.d("PJSIP", "Состояние звонка: ${info.stateText}")
        onStateChanged?.invoke(info.state)
    }

    override fun onCallMediaState(prm: OnCallMediaStateParam?) {
        val callInfo = info
        for (media in callInfo.media) {
            if (media.type == pjmedia_type.PJMEDIA_TYPE_AUDIO && media.status == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE) {

                val audioMedia = getAudioMedia(media.index.toInt())
                val audDevManager = endpoint.audDevManager()

                audioMedia.startTransmit(audDevManager.playbackDevMedia)
                audDevManager.captureDevMedia.startTransmit(audioMedia)

                Log.d("PJSIP", "Аудио подключено")
            }
        }
    }
}
