package com.illuminz.data.models.common.notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

    @field:SerializedName("hasNext")
    val hasNext: Boolean? = null,

    @field:SerializedName("notifications")
    val notifications: List<NotificationsDto>? = null
)