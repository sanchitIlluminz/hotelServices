package com.illuminz.data.remote

import com.illuminz.data.models.common.ApiResponse
import com.illuminz.data.models.response.ServiceDto
import com.illuminz.data.models.response.ServiceCategoryDto
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @GET("getactiveservices")
    suspend fun getServices():Response<ApiResponse<List<ServiceDto>>>

    @GET("getserviceproducts")
    suspend fun getServiceProducts(
        @Query("serviceId") id: String,
        @Query("tag") tag: String
    ): Response<ApiResponse<List<ServiceCategoryDto>>>

}