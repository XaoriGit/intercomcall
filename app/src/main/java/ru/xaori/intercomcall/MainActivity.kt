package ru.xaori.intercomcall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.xaori.intercomcall.presentation.screen.SipScreen
import ru.xaori.intercomcall.presentation.viewmodel.SipViewModel

class MainActivity : ComponentActivity() {

    private val sipViewModel: SipViewModel by viewModel()  // Koin ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализируем SIP при старте активности
        sipViewModel.initSip()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SipScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Завершаем SIP при уничтожении активности
        sipViewModel.destroy()
    }
}
