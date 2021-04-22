package com.illuminz.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceCategoryItemDto(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("itemName")
    val itemName: String? = null,

    @field:SerializedName("itemDescription")
    val itemDescription: String? = null,

    @field:SerializedName("thumbnailPath")
    val image: String? = null,

    @field:SerializedName("price")
    val price: Double? = null,

    @field:SerializedName("vegStatus")
    val vegStatus: Int? = null,

    @field:SerializedName("serviceId")
    val serviceId: String? = null,

    @field:SerializedName("distance")
    val distance: String? = null,

    @field:SerializedName("gallery")
    val gallery: List<String>? = null,

    @field:SerializedName("categoryName")
    val categoryName: String? = null,

    @field:SerializedName("position")
    val position: Int? = null,

    @field:SerializedName("duration")
    val duration: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("ironingPrice")
    val ironingPrice: Double? = null,

    @field:SerializedName("washIroningPrice")
    val washIroningPrice: Double? = null,

    var quantity: Int = 0,

    var laundryType: Int = 0
) : Parcelable