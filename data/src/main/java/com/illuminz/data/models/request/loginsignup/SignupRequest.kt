package com.illuminz.data.models.request.loginsignup

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignupRequest(
    @SerializedName("countryCode")
    var countryCode: Int? = 61,

    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @SerializedName("oldPassword")
    val oldPassword: String? = null,

    @SerializedName("newPassword")
    val newPassword: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("deviceToken")
    val deviceToken: String? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("occupationId")
    val occupationId: String? = null,

    @SerializedName("occupationName")
    val occupationName: String? = null,

    @SerializedName("verificationCode")
    var verificationCode: String? = null,

    @SerializedName("referringCode")
    var referringCode: String? = null

) : Parcelable