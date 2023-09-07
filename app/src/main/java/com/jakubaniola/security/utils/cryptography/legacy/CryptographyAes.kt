package com.jakubaniola.security.utils.cryptography.legacy

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

typealias PairIvAndCiphertext = Pair<String, String>

@RequiresApi(Build.VERSION_CODES.M)
class CryptographyAes(
    private val algorithm: String = KeyProperties.KEY_ALGORITHM_AES,
    private val blockMode: String = KeyProperties.BLOCK_MODE_CBC,
    private val padding: String = KeyProperties.ENCRYPTION_PADDING_PKCS7,
) {

    private val keystoreName = "AndroidKeyStore"
    private val keystoreAlias = "secret-aes"
    val transformation = "$algorithm/$blockMode/$padding"

    private val keystore = KeyStore.getInstance(keystoreName).apply {
        load(null)
    }

    private val encryptCipher = Cipher.getInstance(transformation)
        .apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }

    private fun getDecryptCipherForIv(iv: ByteArray) = Cipher.getInstance(transformation)
        .apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }

    private fun getKey(): SecretKey {
        val existingKey = keystore.getEntry(keystoreAlias, null) as KeyStore.SecretKeyEntry?
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(algorithm)
            .apply {
                val spec = KeyGenParameterSpec.Builder(
                    keystoreAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(blockMode)
                    .setEncryptionPaddings(padding)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
                init(spec)
            }
            .generateKey()
    }

    fun encrypt(plaintext: String): PairIvAndCiphertext {
        val bytes = encryptCipher.doFinal(plaintext.toByteArray())
        return Pair(
            Base64.encodeToString(encryptCipher.iv, Base64.DEFAULT),
            Base64.encodeToString(bytes, Base64.DEFAULT)
        )
    }

    fun decrypt(iv: String, ciphertext: String): String {
        val encryptedData = Base64.decode(ciphertext, Base64.DEFAULT)
        val ivBytes = Base64.decode(iv, Base64.DEFAULT)
        val decodedData = getDecryptCipherForIv(ivBytes).doFinal(encryptedData)
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
}