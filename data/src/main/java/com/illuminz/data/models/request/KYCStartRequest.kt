package com.illuminz.data.models.request

import com.google.gson.annotations.SerializedName

data class KYCStartRequest(

    @field:SerializedName("address")
    val address: AddressDto? = null,

    @field:SerializedName("dob")
    val dob: String? = null,

    @field:SerializedName("given_name")
    val givenName: String? = null,

    @field:SerializedName("middle_name")
    val middleName: String? = null,

    @field:SerializedName("family_name")
    val familyName: String? = null
)