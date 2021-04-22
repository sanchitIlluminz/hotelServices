package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class GuestInfoResponse(
    @SerializedName("userInfo")
    val userInfo: UserInfoDto? = null,

    @SerializedName("settings")
    val settings: SettingsDto? = null,

    @SerializedName("buffet")
    val buffet: List<BuffetDto>? = null

)
