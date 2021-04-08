package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class TaxesResponse(
    @field:SerializedName("foodTaxes")
    val foodTaxes: List<TaxesDto>? = null,

    @field:SerializedName("liquorTaxes")
    val liquorTaxes: List<TaxesDto>? = null
)
