package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class FeedbackResponse(
    @SerializedName("rating")
    val rating: Int? = null,

    @SerializedName("roomNumber")
    val roomNumber: Int? = null,

    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("comment")
    val comment: String? = null,

    @SerializedName("groupCode")
    val groupCode: String? = null,

    @SerializedName("nModified")
    val nModified: Int? = null,

    @SerializedName("ok")
    val ok: Int? = null,

    @SerializedName("n")
    val n: Int? = null
)
