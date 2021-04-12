package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class SaveFoodOrderResponse (
    @field:SerializedName("status")
    private val status:Int? = null,

    @field:SerializedName("requestedTime")
    private val requestedTime:Int? = null,

    @field:SerializedName("orderType")
    private val orderType:Int? = null,

    @field:SerializedName("_id")
    private val _id:String? = null,

    @field:SerializedName("room")
    private val room:Int? = null,

    @field:SerializedName("groupCode")
    private val groupCode:String? = null,

    @field:SerializedName("orderDetail")
    private val orderDetail:FoodCartResponse? = null
)