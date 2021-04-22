package com.illuminz.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BuffetDto(
    @SerializedName("startTime")
    val startTime: Int? = null,

    @SerializedName("endTime")
    val endTime: Int? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null
):Parcelable
