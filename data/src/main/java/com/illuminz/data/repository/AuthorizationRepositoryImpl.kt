package com.illuminz.data.repository

import android.content.SharedPreferences
import com.illuminz.data.models.user.UserDto
import javax.inject.Inject

class AuthorizationRepositoryImpl @Inject constructor(private val preferences: SharedPreferences) :
    AuthorizationRepository {
    companion object {
        private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
        private const val KEY_AUTHORIZATION = "KEY_AUTHORIZATION"
    }

    override fun saveAuthorization(profile: UserDto?) {
        if (profile == null) {
            clear()
            return
        }
        val accessToken = profile.token
        val authorization = "bearer ${profile.token}"
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_AUTHORIZATION, authorization)
            .apply()
    }

    override fun getAuthorization(): String? {
        return preferences.getString(KEY_AUTHORIZATION, null)
    }

    override fun getAccessToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun clear() {
        preferences.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_AUTHORIZATION)
            .apply()
    }
}