package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class ServiceRequestDto(
    @field:SerializedName("groupCode")
    val groupCode: String? = null,

    @field:SerializedName("packageName")
    val packageName: String? = null,

    @field:SerializedName("_id")
    val _id: String? = null,

    @field:SerializedName("packageId")
    val packageId: String? = null,

    @field:SerializedName("detail")
    val detail: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("requestType")
    val requestType: Int? = null
)
