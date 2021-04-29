package com.illuminz.data.models.request

import com.google.gson.annotations.SerializedName

data class ServiceRequest(
    @field: SerializedName("roomNumber")
    val roomNumber:Int? = null,

    @field: SerializedName("groupCode")
    val groupCode:String? = null,

    @field: SerializedName("detail")
    val detail:String? = null,

    @field: SerializedName("requestType")
    val requestType:Int? = null
)
