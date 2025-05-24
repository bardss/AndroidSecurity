package com.jakubaniola.security

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.jakubaniola.security.ui.CryptographyScreen
import com.jakubaniola.security.ui.HomeScreen
import com.jakubaniola.security.ui.LockScreenHandle
import com.jakubaniola.security.ui.theme.SecurityTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blockScreenRecording()
        setContent {
            SecurityTheme {
                var screen: Screen by remember { mutableStateOf(Screen.Home) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LockScreenHandle(
                        context = LocalContext.current,
                        lifecycleOwner = LocalLifecycleOwner.current
                    ) {
                        when (screen) {
                            Screen.Home -> HomeScreen {
                                screen = Screen.Cryptography
                            }
                            Screen.Cryptography -> CryptographyScreen(filesDir) {
                                screen = Screen.Home
                            }
                        }
                    }
                }
            }
        }
    }

    private fun blockScreenRecording() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
}
