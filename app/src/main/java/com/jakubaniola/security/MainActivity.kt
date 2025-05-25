package com.jakubaniola.security

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.jakubaniola.security.ui.navigation.MainNavigation
import com.jakubaniola.security.ui.theme.SecurityTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blockScreenRecording()
        setContent {
            SecurityTheme {
                MainNavigation(filesDir = filesDir)
//                    LockScreenHandle(
//                        context = LocalContext.current,
//                        lifecycleOwner = LocalLifecycleOwner.current
//                    )
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
