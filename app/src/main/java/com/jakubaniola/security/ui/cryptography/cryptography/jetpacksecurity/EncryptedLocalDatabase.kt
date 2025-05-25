package com.jakubaniola.security.ui.cryptography.cryptography.jetpacksecurity

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class EncryptedLocalDatabase(
    private val context: Context,
    private val fileName: String = ENCRYPTED_SHARED_PREFERENCES_FILE_NAME
) {

    val transformation = MasterKey.KeyScheme.AES256_GCM
    private val mainKey = MasterKey.Builder(context)
        .setKeyScheme(transformation)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        fileName,
        mainKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun write(key: String, value: String) {
        with (sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun read(): String = sharedPreferences.all.toString()

    fun readEncryptedFile(): String {
        val dir = File(context.filesDir.parentFile, "shared_prefs")
        val encryptedFile = File(dir, "$fileName.xml")
        return encryptedFile.readText(Charsets.UTF_8)
    }

    fun clearDatabase() {
        with (sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    companion object {
        private const val ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "encrypted-shared-preferences"
    }
}