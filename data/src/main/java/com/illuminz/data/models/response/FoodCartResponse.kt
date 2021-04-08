package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class FoodCartResponse(
    @field:SerializedName("items")
    val items: List<CartItemDto>? = null,

    @field:SerializedName("taxes")
    val taxes: TaxesResponse? = null
)
