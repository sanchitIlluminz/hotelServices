package com.illuminz.data.models.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartRequest(
    @field: SerializedName("room")
    val room:Int? = null,

    @field: SerializedName("groupCode")
    val groupCode:String? = null,

    @field: SerializedName("itemlist")
    val itemList:List<CartItemDetail>? = null
):Parcelable
