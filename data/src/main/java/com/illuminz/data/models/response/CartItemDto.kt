package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class CartItemDto(
    @field:SerializedName("itemType")
    val itemType: Int? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("itemName")
    val itemName: String? = null,

    @field:SerializedName("price")
    val price: Double? = null,

    @field:SerializedName("vegStatus")
    val vegStatus: Int? = null,

    @field:SerializedName("quantity")
    var quantity: Int? = null,

    @field:SerializedName("cartTotal")
    val cartTotal: Double? = null
)
