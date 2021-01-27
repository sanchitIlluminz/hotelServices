package com.illuminz.data.models.user

import com.google.gson.annotations.SerializedName

data class OccupationDto(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
