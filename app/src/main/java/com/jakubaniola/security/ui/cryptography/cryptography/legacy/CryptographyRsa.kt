package com.jakubaniola.security.ui.cryptography.cryptography.legacy

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.math.BigInteger
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.Calendar
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal

class CryptographyRsa(
    private val context: Context,
    private val algorithm: String = "RSA",
    private val blockMode: String = KeyProperties.BLOCK_MODE_ECB,
    private val padding: String = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1,
) {

    private val keystoreName = "AndroidKeyStore"
    private val keystoreAlias = "secret-rsa"
    val transformation = "$algorithm/$blockMode/$padding"

    private val cipher: Cipher = Cipher.getInstance(transformation)

    private val keystore = KeyStore.getInstance(keystoreName).apply {
        load(null)
    }

    private fun createAndroidKeyStoreAsymmetricKey(alias: String): KeyPair {
        val generator = KeyPairGenerator.getInstance(algorithm, keystoreName)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initGeneratorWithKeyGenParameterSpec(generator, alias)
        } else {
            initGeneratorWithKeyPairGeneratorSpec(generator, alias)
        }

        // Generates Key with given spec and saves it to the KeyStore
        return generator.generateKeyPair()
    }

    private fun initGeneratorWithKeyPairGeneratorSpec(generator: KeyPairGenerator, alias: String) {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.YEAR, 20)

        val builder = KeyPairGeneratorSpec.Builder(context)
            .setAlias(alias)
            .setSerialNumber(BigInteger.ONE)
            .setSubject(X500Principal("CN=${alias} CA Certificate"))
            .setStartDate(startDate.time)
            .setEndDate(endDate.time)

        generator.initialize(builder.build())
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun initGeneratorWithKeyGenParameterSpec(generator: KeyPairGenerator, alias: String) {
        val builder = KeyGenParameterSpec.Builder(
            alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(blockMode)
            .setEncryptionPaddings(padding)
        generator.initialize(builder.build())
    }

    private fun getAndroidKeyStoreAsymmetricKeyPair(alias: String): KeyPair? {
        val privateKey = keystore.getKey(alias, null) as PrivateKey?
        val publicKey = keystore.getCertificate(alias)?.publicKey

        return if (privateKey != null && publicKey != null) {
            KeyPair(publicKey, privateKey)
        } else {
            null
        }
    }

    private fun removeAndroidKeyStoreKey(alias: String) = keystore.deleteEntry(alias)

    fun encrypt(plaintext: String): String {
        val publicKey = createAndroidKeyStoreAsymmetricKey(keystoreAlias).public
        return encrypt(plaintext, publicKey)
    }

    private fun encrypt(plaintext: String, key: Key): String {
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val bytes = cipher.doFinal(plaintext.toByteArray())
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun decrypt(ciphertext: String): String {
        val privateKey = getAndroidKeyStoreAsymmetricKeyPair(keystoreAlias)?.private
        return if (privateKey != null) {
            decrypt(ciphertext, privateKey)
        } else "Error: Private key is null"
    }

    private fun decrypt(ciphertext: String, key: Key): String {
        cipher.init(Cipher.DECRYPT_MODE, key)
        val encryptedData = Base64.decode(ciphertext, Base64.DEFAULT)
        val decodedData = cipher.doFinal(encryptedData)
        return String(decodedData)
    }
}