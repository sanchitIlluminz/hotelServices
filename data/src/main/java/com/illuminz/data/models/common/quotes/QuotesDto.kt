package com.illuminz.data.models.common.quotes

import com.google.gson.annotations.SerializedName

data class QuotesDto(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("quote")
    val quote: String? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)