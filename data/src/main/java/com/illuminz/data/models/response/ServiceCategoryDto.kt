package com.illuminz.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceCategoryDto(
   @field:SerializedName("_id")
   val id: String? = null,

   @field:SerializedName("serviceId")
   val serviceId: String? = null,

   @field:SerializedName("categoryName")
   val categoryName: String? = null,

   @field:SerializedName("position")
   val position: Int? = null,

   @field:SerializedName("price")
   val price: Double? = null,

   @field:SerializedName("itemsArr")
   val itemsArr: List<ServiceCategoryItemDto>? = null,

   var quantity:Int = 0
):Parcelable