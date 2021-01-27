package com.illuminz.data.repository

import com.illuminz.data.models.user.UserDto

interface AuthorizationRepository {
    fun saveAuthorization(profile: UserDto?)
    fun getAuthorization(): String?
    fun getAccessToken(): String?
    fun clear()
}