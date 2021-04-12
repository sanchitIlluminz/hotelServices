package com.illuminz.data.models.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItemDetail(
    @field:SerializedName("id")
    val id:String? =null,

    @field:SerializedName("quantity")
    var quantity:Int? =null,

    @field:SerializedName("type")
    var type:Int? =null
): Parcelable
