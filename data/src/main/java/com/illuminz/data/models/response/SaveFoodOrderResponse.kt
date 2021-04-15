package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class SaveFoodOrderResponse (
    @field:SerializedName("status")
    val status:Int? = null,

    @field:SerializedName("requestedTime")
    val requestedTime:Int? = null,

    @field:SerializedName("orderType")
    val orderType:Int? = null,

    @field:SerializedName("_id")
    val _id:String? = null,

    @field:SerializedName("room")
    val room:Int? = null,

    @field:SerializedName("groupCode")
    val groupCode:String? = null,


    @field:SerializedName("orderDetail")
    val orderDetail:FoodCartResponse? = null
)