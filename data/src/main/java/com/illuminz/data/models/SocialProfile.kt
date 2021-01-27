package com.illuminz.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SocialProfile(
    val fragmentType: String,
    val type: String,
    var token: String,
    val socialId: String,
    val fullName: String,
    val firstName: String,
    val lastName: String,
    val image: String?,
    val email: String?,
    var authId: String?
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "", "", "", "")
}