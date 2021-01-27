package com.illuminz.data.models.sendmoney

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MetaResponse(
    @field: SerializedName("timestamp")
    val timestamp: Double?=null,

    @field: SerializedName("rate")
    val rate: Double?=null

): Parcelable
