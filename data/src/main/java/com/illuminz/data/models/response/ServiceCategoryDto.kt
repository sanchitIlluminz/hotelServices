package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class ServiceCategoryDto(
   @field:SerializedName("_id")
   val id: String? = null,

   @field:SerializedName("serviceId")
   val serviceId: String? = null,

   @field:SerializedName("categoryName")
   val categoryName: String? = null,

   @field:SerializedName("position")
   val position: Int? = null,

   @field:SerializedName("duration")
   val duration: Int? = null,

   @field:SerializedName("price")
   val price: Double? = null,

   @field:SerializedName("title")
   val title: String? = null,

   @field:SerializedName("description")
   val description: String? = null,

   @field:SerializedName("status")
   val status: Int? = null,

   @field:SerializedName("ironingPrice")
   val ironingPrice: Double? = null,

   @field:SerializedName("washIroningPrice")
   val washIroningPrice: Double? = null,

   @field:SerializedName("itemName")
   val itemName: String? = null,

   @field:SerializedName("itemsArr")
   val itemsArr: List<ServiceCategoryItemDto>? = null,

   var quantity:Int = 0
)