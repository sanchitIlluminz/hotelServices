package com.illuminz.data.models.master

import com.google.gson.annotations.SerializedName

data class MasterDataResponse(

    @field:SerializedName("purpose_money_sending")
    val purposeMoneySending: List<MoneyTransferToCurrencyItem>? = null,

    @field:SerializedName("payment_type")
    val paymentType: List<MoneyTransferToCurrencyItem>? = null,

    @field:SerializedName("account_type")
    val accountType: List<MoneyTransferToCurrencyItem>? = null,

    @field:SerializedName("kyc_verification_type")
    val kycVerificationType: List<MoneyTransferToCurrencyItem>? = null,

    @field:SerializedName("pagination_limit")
    val paginationLimit: List<MoneyTransferToCurrencyItem>? = null,

    @field:SerializedName("payment_status")
    val paymentStatus: List<MoneyTransferToCurrencyItem>? = null,

    @field:SerializedName("street_type")
    val streetType: List<MoneyTransferToCurrencyItem>? = null,

    @field:SerializedName("country")
    val country: List<MoneyTransferToCurrencyItem>? = null,

    @field:SerializedName("money_transfer_to_currency")
    val moneyTransferToCurrency: List<MoneyTransferToCurrencyItem?>? = null,

    @field:SerializedName("nigeria_bank_list")
    val nigeriaBankList: List<MoneyTransferToCurrencyItem>? = null
)