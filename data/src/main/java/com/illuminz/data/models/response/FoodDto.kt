package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class FoodDto(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("itemName")
    val name: String? = null,

    @field:SerializedName("itemDescription")
    val description: String? = null,

    @field:SerializedName("thumbnailPath")
    val image: String? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("vegStatus")
    val vegStatus: Int? = null,

    var quantity:Int = 0
)
