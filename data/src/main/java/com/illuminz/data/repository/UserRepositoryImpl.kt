package com.illuminz.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.illuminz.data.extensions.toApiError
import com.illuminz.data.extensions.toApiFailure
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.CartRequest
import com.illuminz.data.models.request.OrderListingRequest
import com.illuminz.data.models.response.*
import com.illuminz.data.remote.SocketManager
import com.illuminz.data.remote.UserApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val authorizationRepository: AuthorizationRepository,
    private val preferences: SharedPreferences,
    private val gson: Gson,
    private val socketManager: SocketManager
) : UserRepository {
    companion object {
        private const val KEY_PROFILE = "KEY_PROFILE"
        private const val KEY_LOGIN = "KEY_LOGIN"
    }

    override suspend fun getServices(): Resource<List<ServiceDto>> {
        return try {
            val response = userApi.getServices()
            if (response.isSuccessful){
                Resource.success(response.body()?.data)
            }else{
                Resource.error(response.toApiError())
            }
        }catch (throwable:Throwable){
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getServiceProduct(id: String, tag: String
    ): Resource<List<ServiceCategoryItemDto>> {
//    return Resource.success()
    return try {
            val response = userApi.getServiceProducts(id,tag)
            if (response.isSuccessful){
                Resource.success(response.body()?.data)
            }else{
                Resource.error(response.toApiError())
            }
        }catch (throwable:Throwable){
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getFoodProduct(
        id: String,
        tag: String
    ): Resource<List<ServiceCategoryDto>> {
        return try {
            val response = userApi.getFoodProducts(id,tag)
            if (response.isSuccessful){
                Resource.success(response.body()?.data)
            }else{
                Resource.error(response.toApiError())
            }
        }catch (throwable:Throwable){
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getFoodCart(cartRequest: CartRequest) :Resource<FoodCartResponse>{
       return try {
           val response = userApi.getFoodCart(cartRequest)
           if (response.isSuccessful){
               Resource.success(response.body()?.data)
           }else{
               Resource.error(response.toApiError())
           }
       }catch (throwable:Throwable){
           Resource.error(throwable.toApiFailure())
       }
    }

    override suspend fun getLaundryCart(cartRequest: CartRequest): Resource<LaundryCartResponse> {
        return try {
            val response = userApi.getLaundryCart(cartRequest)
            if (response.isSuccessful){
                Resource.success(response.body()?.data)
            }else{
                Resource.error(response.toApiError())
            }
        }catch (throwable:Throwable){
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun saveFoodOrder(cartRequest: CartRequest): Resource<SaveFoodOrderResponse> {
        return try {
            val response = userApi.saveFoodOrder(cartRequest)
            if (response.isSuccessful){
                Resource.success(response.body()?.data)
            }else{
                Resource.error(response.toApiError())
            }
        }catch (throwable:Throwable){
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun saveLaundryOrder(cartRequest: CartRequest): Resource<SaveLaundryOrderResponse> {
        return try {
            val response = userApi.saveLaundryOrder(cartRequest)
            if (response.isSuccessful){
                Resource.success(response.body()?.data)
            }else{
                Resource.error(response.toApiError())
            }
        }catch (throwable:Throwable){
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getOrderListing(orderListingRequest: OrderListingRequest): Resource<OrderListingResponse> {
        return try {
            val response = userApi.getOrderListing(
                room = 111,
                groupCode = "111"
//                status = orderListingRequest.status?.or(-1),
//                orderType = orderListingRequest.orderType?.or(0),
//                page = orderListingRequest.page?.or(-1)
            )
            if (response.isSuccessful){
                Resource.success(response.body()?.data)
            }else{
                Resource.error(response.toApiError())
            }
        }catch (throwable:Throwable){
            Resource.error(throwable.toApiFailure())
        }
    }
}