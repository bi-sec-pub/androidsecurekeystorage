package com.bisec.securekeystorage.util

import android.hardware.biometrics.BiometricManager
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class KeystoreManager {

    companion object {
        private val NULL = null
        private const val EMPTY = ""
        private const val NEW_LINE = "\n"

        private const val PROVIDER = "AndroidKeyStore"

        private const val PURPOSE = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

        private val ENCODER = Base64.getEncoder().withoutPadding()
        private val DECODER = Base64.getDecoder()
    }

    /**
     * Encrypts the given data object
     *
     * @param decryption Data object to encrypt
     */
    fun encrypt(decryptionData: DecryptionData): EncryptionData {
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM, PROVIDER)

        val keyGenParameterSpec: KeyGenParameterSpec
            = KeyGenParameterSpec.Builder(
            decryptionData.alias, PURPOSE
            )
            .setBlockModes(BLOCK_MODE)
            .setEncryptionPaddings(PADDING)
            .setUserAuthenticationRequired(false)
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        val secretKey: SecretKey = keyGenerator.generateKey()

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedBytes = cipher.doFinal(decryptionData.data.toByteArray())
        val data    = ENCODER.encodeToString(encryptedBytes)
        val iv      = ENCODER.encodeToString(cipher.iv)

        return EncryptionData(
            data = data.replace(NEW_LINE, EMPTY),
            iv = iv.replace(NEW_LINE, EMPTY),
            alias = decryptionData.alias
        )
    }

    /**
     * Decrypts the given data object.
     *
     * @param encryptionData Data object to decrypt
     */
    fun decrypt(encryptionData: EncryptionData): DecryptionData {

        val iv      = DECODER.decode(encryptionData.iv)
        val data    = DECODER.decode(encryptionData.data)

        val keyStore: KeyStore = KeyStore.getInstance(PROVIDER)
        keyStore.load(NULL)

        val secretKeyEntry: SecretKeyEntry =
            keyStore.getEntry(encryptionData.alias, NULL) as SecretKeyEntry

        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(Cipher.DECRYPT_MODE, secretKeyEntry.secretKey, IvParameterSpec(iv))

        val decryptedBytes = String(cipher.doFinal(data))

        return DecryptionData(
            alias = encryptionData.alias,
            data = decryptedBytes
        )
    }

}