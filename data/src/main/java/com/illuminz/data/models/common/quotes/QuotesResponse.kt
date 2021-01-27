package com.illuminz.data.models.common.quotes

import com.google.gson.annotations.SerializedName

data class QuotesResponse(

    @field:SerializedName("hasNext")
    val hasNext: Boolean? = null,

    @field:SerializedName("quotes")
    val quotes: List<QuotesDto>? = null
)