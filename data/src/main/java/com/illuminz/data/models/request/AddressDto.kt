package com.illuminz.data.models.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressDto(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("unit_number")
    val unitNumber: String? = null,

    @field:SerializedName("street_number")
    val streetNumber: String? = null,

    @field:SerializedName("locality")
    val locality: String? = null,

    @field:SerializedName("street_type")
    val streetType: String? = null,

    @field:SerializedName("region")
    val region: String? = null,

    @field:SerializedName("postal_code")
    val postalCode: String? = null,

    @field:SerializedName("street_name")
    val streetName: String? = null
) : Parcelable