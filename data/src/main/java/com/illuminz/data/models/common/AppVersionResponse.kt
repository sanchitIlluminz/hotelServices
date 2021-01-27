package com.illuminz.data.models.common

import com.google.gson.annotations.SerializedName

data class AppVersionResponse(
    @field:SerializedName("androidCriticalVersion")
    val androidCriticalVersion: Int? = null
)