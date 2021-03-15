package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class ServiceProductDto(
   @field:SerializedName("_id")
   val id:String? = null,

   @field:SerializedName("serviceId")
   val serviceId:String? = null,

   @field:SerializedName("categoryName")
   val categoryName:String? = null,

   @field:SerializedName("position")
   val position:Int? = null,

   @field:SerializedName("status")
   val status:Int? = null,

   @field:SerializedName("itemsArr")
   val itemsArr:List<FoodDto>? = null
)
