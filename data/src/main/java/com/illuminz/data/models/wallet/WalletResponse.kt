package com.illuminz.data.models.wallet

import com.google.gson.annotations.SerializedName
import com.illuminz.data.models.transfers.TransactionDto

data class WalletResponse(
    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("pages")
    val pages: Int? = null,

    @field:SerializedName("limit")
    val limit: Int? = null,

    @field:SerializedName("transaction")
    val transaction: List<TransactionDto>? = null,

    @field:SerializedName("walletAmount")
    val walletAmount: Int?=null
)
