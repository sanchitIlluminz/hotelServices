package com.illuminz.data.repository

import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.FoodCartRequest
import com.illuminz.data.models.response.*

interface UserRepository {
    suspend fun getServices(): Resource<List<ServiceDto>>
    suspend fun getServiceProduct(id: String, tag:String): Resource<List<ServiceCategoryItemDto>>
    suspend fun getFoodProduct(id: String, tag:String): Resource<List<ServiceCategoryDto>>
    suspend fun getFoodCart(foodCartRequest: FoodCartRequest): Resource<FoodCartResponse>
    suspend fun saveFoodOrder(foodCartRequest: FoodCartRequest): Resource<SaveOrderResponse>
}