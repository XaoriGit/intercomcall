package ru.xaori.intercomcall.data.pjsip

import org.pjsip.pjsua2.Account
import org.pjsip.pjsua2.AudioMedia
import org.pjsip.pjsua2.Call
import org.pjsip.pjsua2.Endpoint
import org.pjsip.pjsua2.OnCallMediaStateParam
import org.pjsip.pjsua2.pjmedia_type

class MyCall(
    account: Account,
    callId: Int,
    private val ep: Endpoint
) : Call(account, callId) {

    override fun onCallMediaState(prm: OnCallMediaStateParam) {
        super.onCallMediaState(prm)
        val ci = info
        ci.media.forEach { media ->
            if (media.type == pjmedia_type.PJMEDIA_TYPE_AUDIO) {
                val audioMedia = AudioMedia.typecastFromMedia(getMedia(media.index))
                // Подключаем аудио к устройству
                ep.audDevManager().captureDevMedia.startTransmit(audioMedia)
                audioMedia.startTransmit(ep.audDevManager().playbackDevMedia)
            }
        }
    }
}