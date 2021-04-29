package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class ServiceRequestResponse(
    @field: SerializedName("service_request")
    val serviceList: List<ServiceRequestDto>? = null,

    @field: SerializedName("spa_massage_request")
    val massageList: List<ServiceRequestDto>? = null
)
