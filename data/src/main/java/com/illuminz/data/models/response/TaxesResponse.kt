package com.illuminz.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaxesResponse(
    @field:SerializedName("foodTaxes")
    val foodTaxes: List<TaxesDto>? = null,

    @field:SerializedName("liquorTaxes")
    val liquorTaxes: List<TaxesDto>? = null,

    @field:SerializedName("laundardyTaxes")
    val laundryTaxes: List<TaxesDto>? = null
):Parcelable
