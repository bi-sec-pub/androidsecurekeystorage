package com.bisec.securekeystorage.util;

object Keys {
    init {
        System.loadLibrary("key-lib");
    }

    external fun apiKey() : String
}
