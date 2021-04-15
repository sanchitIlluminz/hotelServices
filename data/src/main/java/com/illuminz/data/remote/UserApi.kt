package com.illuminz.data.remote

import com.illuminz.data.models.common.ApiResponse
import com.illuminz.data.models.request.CartRequest
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
    suspend fun getFoodCart(@Body request: CartRequest): Response<ApiResponse<FoodCartResponse>>

    @POST("getlaundarycart")
    suspend fun getLaundryCart(@Body request: CartRequest): Response<ApiResponse<LaundryCartResponse>>

    @POST("savefoodorder")
    suspend fun saveFoodOrder(@Body request: CartRequest): Response<ApiResponse<SaveFoodOrderResponse>>

    @POST("savelaundaryorder")
    suspend fun saveLaundryOrder(@Body request: CartRequest): Response<ApiResponse<SaveLaundryOrderResponse>>

    @GET("order/listing")
    suspend fun getOrderListing(
        @Query("room") room: Int?,
        @Query("groupCode") groupCode:String?
//        @Query("orderType") orderType:Int?,
//        @Query("status") status:Int?,
//        @Query("room") page:Int?
    ):Response<ApiResponse<OrderListingResponse>>
}