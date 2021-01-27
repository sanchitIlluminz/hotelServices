package com.illuminz.data.models.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.illuminz.data.models.request.AddressDto
import com.illuminz.data.models.request.ExchangeRateDto
import com.illuminz.data.models.sendmoney.MetaResponse
import com.illuminz.data.models.sendmoney.TransactionDto
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDto(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("email")
    var email: String? = null,

    @field:SerializedName("referralCode")
    val referralCode: String?,

    @field:SerializedName("wallet")
    val wallet: Double? = null,

    @field:SerializedName("mobile_number")
    val mobile_number: String? = null,

    @field:SerializedName("country_code")
    val country_code: String? = null,

    @field:SerializedName("address")
    var address: AddressDto? = null,

    @field:SerializedName("kyc_status")
    var kyc_status: Int? = null,

    @field:SerializedName("kyc_token")
    val kyc_token: String? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("request")
    val request: ExchangeRateDto? = null,

    @field:SerializedName("meta")
    val meta: MetaResponse? = null,

    @field:SerializedName("response")
    val recepientFund: String? = null,

    @field:SerializedName("transaction")
    val transaction: TransactionDto? = null

) : Parcelable