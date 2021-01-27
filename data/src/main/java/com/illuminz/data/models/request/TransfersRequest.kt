package com.illuminz.data.models.request

import com.google.gson.annotations.SerializedName

data class TransfersRequest(

    @field:SerializedName("pageNumber")
    val pageNumber: Int? = null,

    @field:SerializedName("beneficiaryName")
    val beneficiaryName: String? = null,

    @field:SerializedName("accountNumber")
    val accountNumber: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("paymentType")
    val paymentType: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)