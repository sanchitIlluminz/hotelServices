package com.illuminz.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.illuminz.data.extensions.toApiError
import com.illuminz.data.extensions.toApiFailure
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.*
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
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getServiceProduct(
        id: String, tag: String
    ): Resource<List<ServiceCategoryItemDto>> {
//    return Resource.success()
        return try {
            val response = userApi.getServiceProducts(id, tag)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getFoodProduct(
        id: String,
        tag: String
    ): Resource<List<ServiceCategoryDto>> {
        return try {
            val response = userApi.getFoodProducts(id, tag)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getFoodCart(cartRequest: CartRequest): Resource<FoodCartResponse> {
        return try {
            val response = userApi.getFoodCart(cartRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getLaundryCart(cartRequest: CartRequest): Resource<LaundryCartResponse> {
        return try {
            val response = userApi.getLaundryCart(cartRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun saveFoodOrder(cartRequest: CartRequest): Resource<SaveFoodOrderResponse> {
        return try {
            val response = userApi.saveFoodOrder(cartRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun saveLaundryOrder(cartRequest: CartRequest): Resource<SaveLaundryOrderResponse> {
        return try {
            val response = userApi.saveLaundryOrder(cartRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getOrderListing(orderListingRequest: OrderListingRequest): Resource<OrderListingResponse> {
        return try {
            val response = userApi.getOrderListing(
                room = orderListingRequest.room,
                groupCode = orderListingRequest.groupCode,
//                status = orderListingRequest.status?.or(-1),
                orderType = orderListingRequest.orderType?.or(0)
//                page = orderListingRequest.page?.or(-1)
            )
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getUserInfo(room: Int, groupCode: String): Resource<GuestInfoResponse> {
        return try {
            val response = userApi.getUserInfo(
                roomNumber = room,
                groupCode = groupCode
            )
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getUserFeedback(room: Int, groupCode: String): Resource<FeedbackResponse> {
        return try {
            val response = userApi.getUserFeedback(room, groupCode)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun submitFeedback(feedbackRequest: FeedbackRequest): Resource<FeedbackResponse> {
        return try {
            val response = userApi.submitFeedback(feedbackRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getGymDetails(): Resource<List<ServiceCategoryItemDto>> {
        return try {
            val response = userApi.getGymDetail()
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getNearbyPlacesDetail(): Resource<List<ServiceCategoryItemDto>> {
        return try {
            val response = userApi.getNearbyPlacesDetail()
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun submitMassageRequest(massageRequest: MassageRequest): Resource<Any> {
        return try {
            val response = userApi.submitMassageRequest(massageRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun submitServiceRequest(serviceRequest: ServiceRequest): Resource<Any> {
        return try {
            val response = userApi.submitServiceRequest(serviceRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getServiceRequest(
        roomNumber: Int,
        groupCode: String,
        requestType: Int
    ): Resource<ServiceRequestResponse> {
        return try {
            val response = userApi.getServiceRequest(roomNumber, groupCode, requestType)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }
}