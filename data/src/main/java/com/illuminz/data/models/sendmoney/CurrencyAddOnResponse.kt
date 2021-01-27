package com.illuminz.data.models.sendmoney

import com.google.gson.annotations.SerializedName

data class CurrencyAddOnResponse(
    @field: SerializedName("currency")
    val currency: String? = null,

    @field: SerializedName("currencyAddon")
    val currencyAddon: String? = null,

    @field: SerializedName("actionType")
    val actionType: Int? = null
)
