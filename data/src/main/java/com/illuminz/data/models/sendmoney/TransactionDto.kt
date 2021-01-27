package com.illuminz.data.models.sendmoney

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionDto(
    @field:SerializedName("Success")
    val success: Boolean? = null,

    @field:SerializedName("NavigateURL")
    val navigateURL: String? = null,

    @field:SerializedName("TransactionRefNo")
    val transactionRefNo: String? = null,

    @field:SerializedName("ErrorCode")
    val errorCode: Int? = null
) : Parcelable