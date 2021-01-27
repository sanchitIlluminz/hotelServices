package com.illuminz.data.models.master

import com.google.gson.annotations.SerializedName

data class MoneyTransferToCurrencyItem(

    @field:SerializedName("sign")
    val sign: String? = null,

    @field:SerializedName("label")
    val label: String? = null,

    @field:SerializedName("value")
    val value: String? = null
)