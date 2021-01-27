package com.illuminz.data.models.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReferDetailsResponse(
    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("shareText")
    val shareText: String? = null,

    @field:SerializedName("referralCode")
    val referralCode: String? = null
) : Parcelable