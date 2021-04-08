package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class TaxesDto(
    @field:SerializedName("taxTitle")
    val taxTitle: String? = null,

    @field:SerializedName("taxRate")
    val taxRate: Double? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("debit")
    val debit: Double? = null
)
