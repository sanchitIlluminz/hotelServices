package com.illuminz.data.models.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FcmNotification(
    val type: String?,
    val title: String?,
    val message: String?,
    val messageId: String?,
    val imageUrl: String?,
    val tag: String?,
    val actionId: String?,
    val lastDashboard: String?
) : Parcelable