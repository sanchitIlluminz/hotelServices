package com.illuminz.data.models.sendmoney

import com.google.gson.annotations.SerializedName
import com.illuminz.data.models.sendmoney.CurrencyAddOnResponse

data class CountryResponse(
    @field: SerializedName("countryName")
    val countryName: String? = null,

    @field: SerializedName("countryId")
    val countryId: Int? = null,

    @field: SerializedName("isChipperCash")
    val isChipperCash: Int? = null,

    @field: SerializedName("currencySettings")
    val currencySettings: List<CurrencyAddOnResponse>? = null

)
