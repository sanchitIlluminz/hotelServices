package com.illuminz.data.models.sendmoney

import com.google.gson.annotations.SerializedName

data class AccountDetailsResponse(
    @field: SerializedName("bank_name")
    val bankName: String? = null,

    @field: SerializedName("account_holder_name")
    val accountHolderName: String? = null,

    @field: SerializedName("account_no")
    val accountNo: String? = null,

    @field: SerializedName("unique_code")
    val uniqueCode: String? = null,

    @field: SerializedName("id")
    val id: String? = null,

    @field: SerializedName("charges")
    val charges: String? = null

)
