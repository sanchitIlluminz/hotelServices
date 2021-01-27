package com.illuminz.data.models.transfers

import com.google.gson.annotations.SerializedName

data class ReceiptDto(

    @field:SerializedName("original")
    val original: String? = null
)