package com.illuminz.data.models.transfers

import com.google.gson.annotations.SerializedName

data class PaymentReceiptDto(

    @field:SerializedName("original")
    val original: String? = null
)