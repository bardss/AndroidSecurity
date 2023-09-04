package com.jakubaniola.security.utils

import android.app.KeyguardManager
import android.content.Context

fun Context.isLockScreenSetUp(): Boolean {
    val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    return if (hasMarshmallow()) keyguardManager.isDeviceSecure else keyguardManager.isKeyguardSecure
}