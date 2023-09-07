package com.jakubaniola.security.utils.cryptography.jetpacksecurity

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.File
import java.nio.charset.StandardCharsets

class CryptographyJetpackSecurityFile(
    private val applicationContext: Context,
    private val fileDir: File,
    private val secretFileName: String = DEFAULT_SECRET_FILE_NAME
) {

    val transformation = MasterKey.KeyScheme.AES256_GCM
    private val masterKey = MasterKey.Builder(applicationContext)
        .setKeyScheme(transformation)
        .build()
    private val fileEncryptionScheme = EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB

    fun encrypt(plaintext: String): String {
        val file = File(fileDir, secretFileName)
        return encrypt(file, plaintext)
    }

    private fun encrypt(file: File, plaintext: String): String {
        val encryptedFile = EncryptedFile.Builder(
            applicationContext,
            file,
            masterKey,
            fileEncryptionScheme
        ).build()

        if (file.exists()) {
            file.delete()
        }

        val fileContent = plaintext.toByteArray(StandardCharsets.UTF_8)
        encryptedFile.openFileOutput().apply {
            write(fileContent)
            flush()
            close()
        }
        return file.readText(Charsets.UTF_8)
    }

    fun decrypt(): String {
        val file = File(fileDir, secretFileName)
        return decrypt(file)
    }

    private fun decrypt(file: File): String {
        val encryptedFile = EncryptedFile.Builder(
            applicationContext,
            file,
            masterKey,
            fileEncryptionScheme
        ).build()

        return encryptedFile
            .openFileInput()
            .bufferedReader()
            .readText()
    }

    companion object {
        private const val DEFAULT_SECRET_FILE_NAME = "secret-jetpack-security.txt"
    }
}