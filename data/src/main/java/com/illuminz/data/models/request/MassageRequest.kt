package com.illuminz.data.models.request

import com.google.gson.annotations.SerializedName

data class MassageRequest(
    @field: SerializedName("roomNumber")
    val roomNumber:Int? = null,

    @field: SerializedName("groupCode")
    val groupCode:String? = null,

    @field: SerializedName("packageList")
    val itemList:List<CartItemDetail>? = null
)
