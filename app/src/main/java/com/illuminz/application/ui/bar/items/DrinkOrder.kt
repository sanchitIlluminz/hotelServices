package com.illuminz.application.ui.bar.items

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DrinkOrder( val drinkName: String,
                       val price: Double,
                       val drinkVol: String,
                       val drinkType: String?=null,
                       var quantity: Int = 0) :Parcelable
