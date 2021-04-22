package com.illuminz.data.repository

import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.CartRequest
import com.illuminz.data.models.request.FeedbackRequest
import com.illuminz.data.models.request.OrderListingRequest
import com.illuminz.data.models.response.*

interface UserRepository {
    suspend fun getServices(): Resource<List<ServiceDto>>
    suspend fun getServiceProduct(id: String, tag:String): Resource<List<ServiceCategoryItemDto>>

    suspend fun getFoodProduct(id: String, tag:String): Resource<List<ServiceCategoryDto>>
    suspend fun getFoodCart(cartRequest: CartRequest): Resource<FoodCartResponse>

    suspend fun getLaundryCart(cartRequest: CartRequest): Resource<LaundryCartResponse>

    suspend fun saveFoodOrder(cartRequest: CartRequest): Resource<SaveFoodOrderResponse>
    suspend fun saveLaundryOrder(cartRequest: CartRequest): Resource<SaveLaundryOrderResponse>

    suspend fun getOrderListing(orderListingRequest: OrderListingRequest):Resource<OrderListingResponse>

    suspend fun getUserInfo(room:Int, groupCode: String): Resource<GuestInfoResponse>

    suspend fun getuserfeedback(room:Int, groupCode: String): Resource<FeedbackResponse>
    suspend fun submitFeedback(feedbackRequest: FeedbackRequest): Resource<FeedbackResponse>

}