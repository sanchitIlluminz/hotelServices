package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class SettingsDto(
    @SerializedName("checkInTime")
    val checkInTime: Int? = null,

    @SerializedName("checkOutTime")
    val checkOutTime: Int? = null,

    @SerializedName("reception_contact_number")
    val reception_contact_number: String? = null,

    @SerializedName("hotel_contact_number")
    val hotel_contact_number: String? = null,

    @SerializedName("emergency_contact_number")
    val emergency_contact_number: String? = null,

    @SerializedName("wifi_name")
    val wifi_name: String? = null,

    @SerializedName("wifi_password")
    val wifi_password: String? = null
)
