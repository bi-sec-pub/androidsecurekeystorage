package com.bisec.securekeystorage.util
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

object KeystorePreference {
    private const val FILENAME = "__secret_data"

    private lateinit var application: Application

    private val sharedPreferences: SharedPreferences by lazy {
        application.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    }

    /**
     * Initializes the KeystorePreference and links the application.
     * Must be called in the MainActivity
     *
     * @param application Application
     */
    fun init(application: Application) {
        this.application = application
    }

    /**
     * Returns the data of the shared preferences entry by its given alias
     *
     * @param alias Alias/Key of the entry
     *
     * @return If the shared preferences entry exists the data is returned else null
     */
    fun get(alias: String): String? {
        val json: String = sharedPreferences.getString(alias, null) ?: return null

        val jsonObject = JSONObject (json)

        val encryptionData = EncryptionData(
            iv = jsonObject .getString("iv"),
            alias = alias,
            data = jsonObject .getString("data"),
        )

        return KeystoreManager().decrypt(encryptionData).data
    }

    /**
     * Saves the combination of the given key and the encrypted value
     * in the specified shared preferences file.
     *
     * @param key Key of the entry
     * @param value Value of the entry
     *
     * @throws IllegalAccessException If the key already exists
     *
     * @return The encrypted data object
     */
    @Throws
    fun save(key: String, value: String): EncryptionData {

        if (sharedPreferences.contains(key)) {
            throw IllegalAccessException("The key already exists in the file.")
        }

        val decryptionData = DecryptionData(
            alias = key,
            data = value
        )
        val encrypted = KeystoreManager().encrypt(decryptionData)

        val json = JSONObject().apply {
            put("data", encrypted.data)
            put("iv", encrypted.iv)
        }

        sharedPreferences.edit().putString(key, json.toString()).apply()

        return encrypted
    }
}