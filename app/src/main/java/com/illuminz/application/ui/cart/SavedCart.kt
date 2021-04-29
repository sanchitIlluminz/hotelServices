package com.illuminz.application.ui.cart

import android.os.Parcelable
import com.illuminz.data.models.request.CartItemDetail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SavedCart(
    val items: MutableList<CartItemDetail> = mutableListOf()
) : Parcelable