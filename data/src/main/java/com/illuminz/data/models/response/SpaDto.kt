package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class SpaDto(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("itemName")
    val name: String? = null,

    @field:SerializedName("itemDescription")
    val description: String? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    var quantity:Int = 0
)
