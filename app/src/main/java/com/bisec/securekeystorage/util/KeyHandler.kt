package com.bisec.securekeystorage.util

import java.net.HttpURLConnection
import java.net.URL

class KeyHandler {

    fun getSecretApiKey() : String {
        val data = KeystorePreference.get("secretApiKey")

        if(!data.isNullOrEmpty()) {
            return "$data"
        }

        /*
        * Retrieve api key from server
        */
        val url         = "https://pentest.bi-sec.cloud/secret-api-key.php"
        val connection  = URL(url).openConnection() as HttpURLConnection
        val apikey      = connection.inputStream.bufferedReader().readText()

        // Save in shared preferences
        KeystorePreference.save(key = "secretApiKey", apikey)

        return apikey
    }
}