package com.bisec.securekeystorage.util

/**
 * Decryption data object
 *
 * @param alias Alias/Key of the entry
 * @param data Decrypted data of the entry
 */
data class DecryptionData(
    val alias: String,
    val data: String
)
