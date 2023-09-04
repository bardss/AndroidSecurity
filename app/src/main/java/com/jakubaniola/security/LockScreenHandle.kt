package com.jakubaniola.security

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.jakubaniola.security.utils.DoOnLifecycle
import com.jakubaniola.security.utils.isLockScreenSetUp
import com.jakubaniola.security.utils.openLockScreenSettings
import kotlin.system.exitProcess


@Composable
fun LockScreenHandle(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    whenLockScreenSetUp: @Composable () -> Unit,
) {
    var isLockScreenSetUp by remember { mutableStateOf(context.isLockScreenSetUp()) }
    if (!isLockScreenSetUp) {
        LockScreenDialog(context = context)
    } else {
        whenLockScreenSetUp()
    }
    DoOnLifecycle(
        lifecycleOwner = lifecycleOwner,
        lifecycleEvent = Lifecycle.Event.ON_START,
        doOnEvent = {
            isLockScreenSetUp = context.isLockScreenSetUp()
        }
    )
}

@Composable
private fun LockScreenDialog(context: Context) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(text = "Lock Screen")
        },
        text = {
            Text(text = "Lock screen has not been set up. To use the up, set up lock screen on your device.")
        },
        confirmButton = {
            Button(
                onClick = { context.openLockScreenSettings() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            ) {
                Text(text = "Settings")
            }
        },
        dismissButton = {
            Button(
                onClick = { exitProcess(0) }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            ) {
                Text(text = "Exit")
            }
        },
    )
}