package com.illuminz.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryDto(
    @field: SerializedName("thumbnailPath")
    val thumbnailPath: String? = null,

    @field:SerializedName("filePath")
    val filePath: String? = null,

    @field:SerializedName("isExclusive")
    val isExclusive: Int? = null
): Parcelable
