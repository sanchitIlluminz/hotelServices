package com.illuminz.data.remote

import com.illuminz.data.models.common.ApiResponse
import com.illuminz.data.models.request.FoodCartRequest
import com.illuminz.data.models.response.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("getactiveservices")
    suspend fun getServices():Response<ApiResponse<List<ServiceDto>>>

    @GET("getserviceproducts")
    suspend fun getFoodProducts(
        @Query("serviceId") id: String,
        @Query("tag") tag: String
    ): Response<ApiResponse<List<ServiceCategoryDto>>>

    @GET("getserviceproducts")
    suspend fun getServiceProducts(
        @Query("serviceId") id: String,
        @Query("tag") tag: String
    ): Response<ApiResponse<List<ServiceCategoryItemDto>>>

    @POST("getfoodcart")
    suspend fun getFoodCart(@Body request: FoodCartRequest): Response<ApiResponse<FoodCartResponse>>

    @POST("/savefoodorder")
    suspend fun saveFoodOrder(@Body request: FoodCartRequest): Response<ApiResponse<SaveOrderResponse>>
}