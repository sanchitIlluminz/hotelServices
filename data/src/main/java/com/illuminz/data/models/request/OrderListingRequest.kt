package com.illuminz.data.models.request

import com.google.gson.annotations.SerializedName

data class OrderListingRequest(
    @SerializedName("room")
    val room: Int? = null,

    @SerializedName("groupCode")
    val groupCode: String? = null,

    @SerializedName("orderType")
    val orderType: Int? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("page")
    val page: Int? = null
)