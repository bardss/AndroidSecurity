package com.jakubaniola.security.utils

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent


fun Context.openLockScreenSettings() {
    startActivity(
        Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
    )
}