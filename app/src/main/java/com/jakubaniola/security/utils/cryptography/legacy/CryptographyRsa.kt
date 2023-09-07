//package com.jakubaniola.security.utils.cryptography
//
//import android.annotation.TargetApi
//import android.os.Build
//import android.security.KeyPairGeneratorSpec
//import android.security.keystore.KeyGenParameterSpec
//import android.security.keystore.KeyProperties
//import android.util.Base64
//import androidx.annotation.RequiresApi
//import java.io.InputStream
//import java.io.OutputStream
//import java.math.BigInteger
//import java.security.Key
//import java.security.KeyPair
//import java.security.KeyPairGenerator
//import java.security.KeyStore
//import java.security.PrivateKey
//import java.util.Calendar
//import javax.crypto.Cipher
//import javax.crypto.KeyGenerator
//import javax.crypto.SecretKey
//import javax.crypto.spec.IvParameterSpec
//import javax.security.auth.x500.X500Principal
//
//class CryptographyRsa(
//    private val algorithm: String = "RSA",
//    blockMode: String = "ECB",
//    padding: String = "PKCS1Padding",
//) {
//
//    private val keystoreName = "AndroidKeyStore"
//    val transformation = "$algorithm/$blockMode/$padding"
//
//    private val cipher: Cipher = Cipher.getInstance(transformation)
//
//    private val keystore = KeyStore.getInstance(keystoreName).apply {
//        load(null)
//    }
//
//    private fun createAndroidKeyStoreAsymmetricKey(alias: String): KeyPair {
//        val generator = KeyPairGenerator.getInstance(algorithm, keystoreName)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            initGeneratorWithKeyGenParameterSpec(generator, alias)
//        } else {
//            initGeneratorWithKeyPairGeneratorSpec(generator, alias)
//        }
//
//        // Generates Key with given spec and saves it to the KeyStore
//        return generator.generateKeyPair()
//    }
//
//    private fun initGeneratorWithKeyPairGeneratorSpec(generator: KeyPairGenerator, alias: String) {
//        val startDate = Calendar.getInstance()
//        val endDate = Calendar.getInstance()
//        endDate.add(Calendar.YEAR, 20)
//
//        val builder = KeyPairGeneratorSpec.Builder(context)
//            .setAlias(alias)
//            .setSerialNumber(BigInteger.ONE)
//            .setSubject(X500Principal("CN=${alias} CA Certificate"))
//            .setStartDate(startDate.time)
//            .setEndDate(endDate.time)
//
//        generator.initialize(builder.build())
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    private fun initGeneratorWithKeyGenParameterSpec(generator: KeyPairGenerator, alias: String) {
//        val builder = KeyGenParameterSpec.Builder(
//            alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//        )
//            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
//            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
//        generator.initialize(builder.build())
//    }
//
//    private fun getAndroidKeyStoreAsymmetricKeyPair(alias: String): KeyPair? {
//        val privateKey = keystore.getKey(alias, null) as PrivateKey?
//        val publicKey = keystore.getCertificate(alias)?.publicKey
//
//        return if (privateKey != null && publicKey != null) {
//            KeyPair(publicKey, privateKey)
//        } else {
//            null
//        }
//    }
//
//    private fun removeAndroidKeyStoreKey(alias: String) = keystore.deleteEntry(alias)
//
//    private fun encrypt(data: String, key: Key?): String {
//        cipher.init(Cipher.ENCRYPT_MODE, key)
//        val bytes = cipher.doFinal(data.toByteArray())
//        return Base64.encodeToString(bytes, Base64.DEFAULT)
//    }
//
//    private fun decrypt(data: String, key: Key?): String {
//        cipher.init(Cipher.DECRYPT_MODE, key)
//        val encryptedData = Base64.decode(data, Base64.DEFAULT)
//        val decodedData = cipher.doFinal(encryptedData)
//        return String(decodedData)
//    }
//}