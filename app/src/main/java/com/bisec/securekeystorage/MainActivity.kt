package com.bisec.securekeystorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bisec.securekeystorage.util.KeyHandler
import com.bisec.securekeystorage.util.Keys
import com.bisec.securekeystorage.util.KeystorePreference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KeystorePreference.init(application)

        // Display api key from C++ file
        findViewById<TextView>(R.id.txt_via_cpp).text = Keys.apiKey()

        // Extra thread due to the needed network connection
        Thread {
            // Get api key from shared prefs
            val string = KeyHandler().getSecretApiKey()

            runOnUiThread {
                findViewById<TextView>(R.id.txt_via_keystore).text = string
            }
        }.start()
    }
}