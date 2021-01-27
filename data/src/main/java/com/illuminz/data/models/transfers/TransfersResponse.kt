package com.illuminz.data.models.transfers

import com.google.gson.annotations.SerializedName

data class TransfersResponse(

    @field:SerializedName("walletAmount")
    val walletAmount: Double? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("pages")
    val pages: Int? = null,

    @field:SerializedName("limit")
    val limit: Int? = null,

    @field:SerializedName("transaction")
    val transaction: List<TransactionDto>? = null,

    @field:SerializedName("transactions")
    val transactions: List<TransactionDto>? = null
)