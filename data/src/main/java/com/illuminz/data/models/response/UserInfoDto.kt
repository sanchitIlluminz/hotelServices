package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

data class UserInfoDto(
    @SerializedName("guestName")
    val guestName: String? = null,

    @SerializedName("arrivalDate")
    val arrivalDate: ZonedDateTime? = null,

    @SerializedName("departureDate")
    val departureDate: ZonedDateTime? = null,

    @SerializedName("noOfNights")
    val noOfNights: Int? = null
)
