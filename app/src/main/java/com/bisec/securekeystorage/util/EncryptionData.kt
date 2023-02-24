package com.bisec.securekeystorage.util

/**
 * Encryption data object
 *
 * @param alias Alias/Key of the entry
 * @param data Encrypted data of the entry
 * @param iv Initialization vector for encryption
 */
data class EncryptionData(
    val alias: String,
    val data: String,
    val iv: String
)
