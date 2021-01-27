package com.illuminz.data.models.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageResponse(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("extension")
    val extension: String? = null,

    @field:SerializedName("size")
    val size: String? = null,

    @field:SerializedName("sizeInKiloBytes")
    val sizeInKiloBytes: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("location")
    var location: String? = null,

    @field:SerializedName("original")
    val original: String? = null,

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null
) : Parcelable