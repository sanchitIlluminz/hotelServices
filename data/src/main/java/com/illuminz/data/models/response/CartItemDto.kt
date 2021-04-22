package com.illuminz.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItemDto(
    @field:SerializedName("itemType")
    val itemType: Int? = null,

    @field:SerializedName("type")
    val laundryType: Int? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("itemName")
    val itemName: String? = null,

    @field:SerializedName("price")
    val price: Double? = null,

    @field:SerializedName("ironingPrice")
    val ironingPrice: Double? = null,

    @field:SerializedName("washIroningPrice")
    val washIroningPrice: Double? = null,

    @field:SerializedName("vegStatus")
    val vegStatus: Int? = null,

    @field:SerializedName("quantity")
    var quantity: Int? = null,

    @field:SerializedName("cartTotal")
    val cartTotal: Double? = null
):Parcelable
