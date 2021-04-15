package com.illuminz.data.models.response

import com.google.gson.annotations.SerializedName

data class OrderListingResponse(
    @SerializedName("data")
    var data: List<SaveFoodOrderResponse>? = null,

    @SerializedName("totalRecords")
    var totalRecords: Int? = null,

    @SerializedName("page")
    var page: Int? = null,

    @SerializedName("nextPage")
    var nextPage: Int? = null,

    @SerializedName("perPage")
    var perPage: Int? = null
)
