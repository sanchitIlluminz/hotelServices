package com.illuminz.data.models.request

import com.google.gson.annotations.SerializedName

data class KYCManualRequest(
    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("frontAttachmentId")
    val frontAttachmentId: String? = null,

    @field:SerializedName("backAttachmentId")
    val backAttachmentId: String? = null
)