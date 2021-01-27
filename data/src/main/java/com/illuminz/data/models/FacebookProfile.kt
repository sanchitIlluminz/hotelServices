package com.illuminz.data.models

data class FacebookProfile(
    val facebookId: String,
    val token: String,
    val email: String?,
    val profileImage: String,
    val firstName: String,
    val lastName: String,
    val fullName: String
)