package com.illuminz.data.models.common.notification

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

data class NotificationsDto(

    @field:SerializedName("createdAt")
    val createdAt: ZonedDateTime? = null,

    @field:SerializedName("isRead")
    var isRead: Boolean? = null,

    @field:SerializedName("actionId")
    val actionId: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("text")
    val text: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("picture")
    val picture: String? = null,

    @field:SerializedName("body")
    val body: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)