package ru.xaori.intercomcall.data.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.koin.android.ext.android.inject
import org.pjsip.pjsua2.AccountConfig
import org.pjsip.pjsua2.AuthCredInfo
import org.pjsip.pjsua2.Endpoint
import org.pjsip.pjsua2.EpConfig
import org.pjsip.pjsua2.LogEntry
import org.pjsip.pjsua2.LogWriter
import org.pjsip.pjsua2.TransportConfig
import org.pjsip.pjsua2.pjsip_transport_type_e
import ru.xaori.intercomcall.IncomingCallActivity
import ru.xaori.intercomcall.R
import ru.xaori.intercomcall.data.pjsip.MyAccount
import ru.xaori.intercomcall.data.repository.CallController

class SipForegroundService(
    private var logWriter: LogWriter? = null,
    private var sipInitialized: Boolean = false,
) : Service() {

    private val callController: CallController by inject()
    private val ep: Endpoint by inject()
    private var account: MyAccount? = null


    companion object {
        private const val CHANNEL_ID = "sip_foreground_channel"
        private const val NOTIF_ID = 101

        fun startService(
            context: Context,
            accountUri: String,
            registrarUri: String,
            username: String,
            password: String
        ) {
            val intent = Intent(context, SipForegroundService::class.java).apply {
                putExtra("accountUri", accountUri)
                putExtra("registrarUri", registrarUri)
                putExtra("username", username)
                putExtra("password", password)
            }
            context.startForegroundService(intent)
        }
    }

    override fun onDestroy() {
        Log.d("SIP", "*** Destroying SIP...")

        try {
            account?.apply {
                setRegistration(false)
                delete()
            }

            ep.libDestroy()
            ep.delete()
        } catch (e: Exception) {
            Log.e("SIP", "Failed to destroy SIP: ${e.message}")
        }

        Log.d("SIP", "*** SIP destroyed")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("FullScreenIntentPolicy")
    fun showIncomingCallNotification() {
        val fullScreenIntent = Intent(this, IncomingCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, 0, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_call)
            .setContentTitle("Входящий звонок")
            .setContentText("SIP вызов")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setOngoing(true)
            .build()

        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(2000, notification)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            NOTIF_ID,
            buildForegroundNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
        )


        val accountUri = intent?.getStringExtra("accountUri") ?: return START_NOT_STICKY
        val registrarUri = intent.getStringExtra("registrarUri") ?: return START_NOT_STICKY
        val username = intent.getStringExtra("username") ?: return START_NOT_STICKY
        val password = intent.getStringExtra("password") ?: return START_NOT_STICKY

        initSip(accountUri, registrarUri, username, password)
        return START_STICKY
    }

    private fun initSip(
        accountUri: String, registrarUri: String, username: String, password: String
    ) {
        if (sipInitialized) {
            Log.d("SIP", "SIP already initialized - skipping")
            return
        }

        sipInitialized = true

        try {
            ep.apply {
                libCreate()
                val epConfig = EpConfig()

                logWriter = object : LogWriter() {
                    override fun write(entry: LogEntry) {
                        Log.d("PJSIP", entry.msg)
                    }
                }
                epConfig.logConfig.writer = logWriter
                epConfig.logConfig.decor = 0

                epConfig.logConfig.apply {
                    level = 5
                    consoleLevel = 5
                    writer = logWriter
                    decor = 0
                }

                epConfig.uaConfig.stunServer.add("stun.linphone.org:3478")

                libInit(epConfig)
                val tpCfg = TransportConfig()
                transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_TLS, tpCfg)

                libStart()
            }

            val cfg = AccountConfig().apply {
                idUri = accountUri
                regConfig.registrarUri = registrarUri
                sipConfig.authCreds.add(AuthCredInfo("digest", "*", username, 0, password))
                sipConfig.authInitialAlgorithm = "MD5"

                natConfig.iceEnabled = true
                natConfig.turnEnabled = false
                natConfig.contactRewriteUse = 6
            }

            account = MyAccount(
                ep = ep,
                callController = callController,
                serviceContext = this
            ).apply {
                create(cfg)
            }

            Log.d("SIP", "*** SIP initialized")
        } catch (e: Exception) {
            Log.e("SIP", "Failed to init PJSIP: ${e.message}")
        }
    }


    private fun buildForegroundNotification(): Notification {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "SIP Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SIP сервис работает")
            .setContentText("Ожидание звонков…")
            .setSmallIcon(R.drawable.ic_call)
            .setOngoing(true)
            .build()
    }
}