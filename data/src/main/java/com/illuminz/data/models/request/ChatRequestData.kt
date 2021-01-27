package com.illuminz.data.models.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.ZonedDateTime

@Parcelize
data class ChatRequestData(
    var thumbnail: String? = null,
    var name: String? = null,
    var reciverId: String? = null,
    var isActive: Boolean? = null,
    var lastSeen: ZonedDateTime? = null,
    var lastMessageId: String? = null
) : Parcelable