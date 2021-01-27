package com.illuminz.data.models.transfers

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.illuminz.data.models.common.ImageResponse
import com.illuminz.data.models.sendmoney.TransactionDto
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.ZonedDateTime

@Parcelize
data class TransactionDto(

    @field:SerializedName("reason")
    val reason: String? = null,

    @field:SerializedName("transaction_id")
    val transaction_id: String? = null,

    @field:SerializedName("purpose")
    var purpose: String? = null,

    @field:SerializedName("directTransferTransactionId")
    val directTransferTransactionId: String? = null,

    @field:SerializedName("created_at")
    val createdAt: ZonedDateTime? = null,

    @field:SerializedName("transactionStatus")
    val transactionStatus: ZonedDateTime? = null,

    @field:SerializedName("createdAt")
    val createdAtWallet: ZonedDateTime? = null,

    /*@field:SerializedName("payment_receipt")
    val paymentReceipt: Any? = null,*/

    @field:SerializedName("bankName")
    var bankName: String? = null,

    @field:SerializedName("total")
    val total: Double? = null,

    @field:SerializedName("totalAmount")
    var totalAmount: Double? = null,

    @field:SerializedName("beneficiaryName")
    var beneficiaryName: String? = null,

    @field:SerializedName("admin_status")
    val adminStatus: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("_id")
    val _id: String? = null,

    @field:SerializedName("currencyConvertedAmount")
    var currencyConvertedAmount: Double? = null,

    @field:SerializedName("transaction")
    val transaction: TransactionDto? = null,

    @field:SerializedName("currency_to")
    var currencyTo: String? = null,

    @field:SerializedName("currencyCode")
    var currencyCode: String? = null,

    @field:SerializedName("chipperCash")
    val chipperCash: String? = null,

    @field:SerializedName("amount")
    var amount: Double? = null,

    @field:SerializedName("accountType")
    var accountType: Int? = null,

    @field:SerializedName("receiverMobileNo")
    val receiverMobileNo: String? = null,

    @field:SerializedName("accountNumber")
    var accountNumber: String? = null,

    @field:SerializedName("receiverAddress")
    val receiverAddress: String? = null,

    @field:SerializedName("charges")
    var charges: Double? = null,

    @field:SerializedName("transferCharges")
    var transferCharges: Double? = null,

    @field:SerializedName("transferChargesAmount")
    var transferChargesAmount: Double? = null,

    @field:SerializedName("paymentType")
    var paymentType: Int? = null,

    @field:SerializedName("countryToTransfer")
    var countryToTransfer: String? = null,

    @field:SerializedName("country")
    var country: String? = null,

    @field:SerializedName("countryId")
    var countryId: String? = null,


    @field:SerializedName("user_id")
    val userId: Int? = null,

    var uniquePaymentId: String? = null,

    var rateWithoutAddOn: String? = null,
/*
	@field:SerializedName("iban")
	val iban: Any? = null,*/

    @field:SerializedName("receipt")
    val receipt: ImageResponse? = null,

    @field:SerializedName("currencyRate")
    var currencyRate: Double? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("walletAmount")
    var walletAmount: Int? = null,

    @field:SerializedName("wallet_amount")
    var wallet_amount: Int? = null
) : Parcelable