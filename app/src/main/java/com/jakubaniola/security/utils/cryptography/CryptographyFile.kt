package com.jakubaniola.security.utils.cryptography

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.M)
class CryptographyFile(
    private val fileDir: File,
    private val secretFileName: String = SECRET_FILE_NAME
) {

    private val cryptography = Cryptography()

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
        private const val SECRET_FILE_NAME = "secret.txt"
    }
}