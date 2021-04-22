package com.illuminz.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodCartResponse(
    @field:SerializedName("items")
    val items: List<CartItemDto>? = null,

    @field:SerializedName("taxes")
    val taxes: TaxesResponse? = null
):Parcelable
