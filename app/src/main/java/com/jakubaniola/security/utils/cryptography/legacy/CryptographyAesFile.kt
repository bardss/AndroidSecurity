package com.jakubaniola.security.utils.cryptography.legacy

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.M)
class CryptographyAesFile(
    private val fileDir: File,
    private val secretFileName: String = DEFAULT_SECRET_FILE_NAME
) {

    val cryptography = CryptographyAes()

    fun encrypt(plaintext: String): String {
        val bytes = plaintext.encodeToByteArray()
        val file = File(fileDir, secretFileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        val fos = FileOutputStream(file)

        return cryptography.encrypt(
            byteArray = bytes,
            outputStream = fos
        ).decodeToString()
    }

    fun decrypt(): String {
        val file = File(fileDir, secretFileName)
        return cryptography.decrypt(
            inputStream = FileInputStream(file)
        ).decodeToString()
    }

    companion object {
        private const val DEFAULT_SECRET_FILE_NAME = "secret.txt"
    }
}