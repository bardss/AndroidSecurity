@file:OptIn(ExperimentalMaterial3Api::class)

package com.jakubaniola.security

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.jakubaniola.security.ui.CryptographyUi
import com.jakubaniola.security.ui.LockScreenHandle
import com.jakubaniola.security.ui.theme.SecurityTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecurityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LockScreenHandle(
                        context = LocalContext.current,
                        lifecycleOwner = LocalLifecycleOwner.current
                    ) {
                        CryptographyUi(filesDir)
                    }
                }
            }
        }
    }
}