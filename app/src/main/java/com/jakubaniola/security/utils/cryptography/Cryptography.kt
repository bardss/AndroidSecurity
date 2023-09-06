package com.jakubaniola.security.utils.cryptography

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(Build.VERSION_CODES.M)
class Cryptography {

    private val keystoreAlias = "secret"

    private val keystore = KeyStore.getInstance(KEY_STORE_NAME).apply {
        load(null)
    }

    private val encryptCipher = Cipher
        .getInstance(TRANSFORMATION)
        .apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }

    private fun getDecryptCipher() = Cipher
        .getInstance(TRANSFORMATION)
        .apply {
            init(Cipher.DECRYPT_MODE, getKey())
        }

    private fun getDecryptCipherForIv(iv: ByteArray) = Cipher
        .getInstance(TRANSFORMATION)
        .apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }

    private fun getKey(): SecretKey {
        val existingKey = keystore.getEntry(keystoreAlias, null) as SecretKeyEntry?
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator
            .getInstance(ALGORITHM)
            .apply {
                val spec = KeyGenParameterSpec.Builder(
                    keystoreAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
                init(spec)
            }
            .generateKey()
    }

    fun encrypt(plaintext: String): String {
        val bytes = encryptCipher.doFinal(plaintext.toByteArray())
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun decrypt(ciphertext: String): String {
        val encryptedData = Base64.decode(ciphertext, Base64.DEFAULT)
        val decodedData = getDecryptCipher().doFinal(encryptedData)
        return String(decodedData)
    }

    fun encrypt(byteArray: ByteArray, outputStream: OutputStream): ByteArray {
        val encryptedBytes = encryptCipher.doFinal(byteArray)
        outputStream.use {
            it.write(encryptCipher.iv.size)
            it.write(encryptCipher.iv)
            it.write(encryptedBytes.size)
            it.write(encryptedBytes)
        }
        return encryptedBytes
    }

    fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val ivSize = it.read()
            val iv = ByteArray(ivSize)
            it.read(iv)

            val encryptedBytesSize = it.read()
            val encryptedBytes = ByteArray(encryptedBytesSize)
            it.read(encryptedBytes)

            getDecryptCipherForIv(iv).doFinal(encryptedBytes)
        }
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val KEY_STORE_NAME = "AndroidKeyStore"
    }
}