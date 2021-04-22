package com.illuminz.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaxesDto(
    @field:SerializedName("taxTitle")
    val taxTitle: String? = null,

    @field:SerializedName("taxRate")
    val taxRate: Double? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("debit")
    val debit: Double? = null
):Parcelable
