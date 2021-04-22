package com.illuminz.data.models.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FeedbackRequest(
    @field: SerializedName("roomNumber")
    val room: Int? = null,

    @field: SerializedName("groupCode")
    val groupCode: String? = null,

    @field: SerializedName("rating")
    val rating: Int? = null,

    @field: SerializedName("comment")
    val comment: String? = null,

    @field: SerializedName("id")
    val id: String? = null
)
