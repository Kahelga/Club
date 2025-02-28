package com.example.club.authorization.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {
    private val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_app_preferences",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    private val ACCESS_TOKEN_KEY = "access_token"
    private val REFRESH_TOKEN_KEY = "refresh_token"
    private val EXPIRATION_TIME_KEY = "expiration_time"

    fun saveTokens(accessToken: String?, refreshToken: String, expiresIn: Long) {
        with(sharedPreferences.edit()) {
            putString(ACCESS_TOKEN_KEY, accessToken)
            putString(REFRESH_TOKEN_KEY, refreshToken)
            putLong(EXPIRATION_TIME_KEY, System.currentTimeMillis() + expiresIn * 1000)
            apply()
        }
    }
    fun updateAccessToken(accessToken: String?) {
        with(sharedPreferences.edit()) {
            accessToken?.let { putString(ACCESS_TOKEN_KEY, it) }
            remove(REFRESH_TOKEN_KEY)
            remove(EXPIRATION_TIME_KEY)
            apply()

        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    fun isAccessTokenExpired(): Boolean {
        val expirationTime = sharedPreferences.getLong(EXPIRATION_TIME_KEY, 0)
        return System.currentTimeMillis() >= expirationTime
    }

    fun clearTokens() {
        with(sharedPreferences.edit()) {
            remove(ACCESS_TOKEN_KEY)
            remove(REFRESH_TOKEN_KEY)
            remove(EXPIRATION_TIME_KEY)
            apply()
        }
    }
}
