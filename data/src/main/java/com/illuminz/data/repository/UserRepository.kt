package com.illuminz.data.repository

import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.ServiceDto
import com.illuminz.data.models.response.ServiceCategoryDto
import com.illuminz.data.models.response.ServiceCategoryItemDto

interface UserRepository {
    suspend fun getServices(): Resource<List<ServiceDto>>
    suspend fun getServiceProduct(id: String, tag:String): Resource<List<ServiceCategoryItemDto>>
    suspend fun getFoodProduct(id: String, tag:String): Resource<List<ServiceCategoryDto>>
}