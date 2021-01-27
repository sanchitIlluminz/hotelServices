package com.illuminz.data.models.common

import com.google.gson.annotations.SerializedName

data class ReferDetailForBoth(
    @field:SerializedName("studentRatingDetails")
    val studentRatingDetails: ReferDetailsResponse? = null,
    @field:SerializedName("teacherRatingDetails")
    val teacherRatingDetails: ReferDetailsResponse? = null
)