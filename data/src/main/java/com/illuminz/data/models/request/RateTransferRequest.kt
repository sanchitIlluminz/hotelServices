package com.illuminz.data.models.request

import com.google.gson.annotations.SerializedName

data class RateTransferRequest(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("rating")
    val rating: Float? = null,

    @field:SerializedName("feedback")
    val feedback: String? = null
)