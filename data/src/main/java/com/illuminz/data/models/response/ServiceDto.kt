package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class ServiceDto(
    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("_id")
    val id:String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("detail")
    val detail: String? = null,

    @field:SerializedName("tag")
    val tag: String? = null,

    @field:SerializedName("imagePath")
    val imagePath: String? =null
)
