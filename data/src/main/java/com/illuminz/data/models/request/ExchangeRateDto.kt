package com.illuminz.data.models.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExchangeRateDto(
    @field: SerializedName("amount")
    var amount: Double?=null,

    @field: SerializedName("from")
    var from: String? = null,

    @field: SerializedName("to")
    var to: String? = null
):Parcelable
