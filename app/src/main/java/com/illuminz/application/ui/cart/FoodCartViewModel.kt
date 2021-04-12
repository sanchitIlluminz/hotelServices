package com.illuminz.application.ui.cart

import androidx.lifecycle.LiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.AppConstants
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.CartRequest
import com.illuminz.data.models.response.FoodCartResponse
import com.illuminz.data.models.response.SaveFoodOrderResponse
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FoodCartViewModel  @Inject constructor(
    private val userRepository: UserRepository,
    private val cartHandler: CartHandler
) : BaseViewModel() {
    private val foodCartObserver by lazy { SingleLiveEvent<Resource<FoodCartResponse>>() }
    private val saveCartObserver by lazy { SingleLiveEvent<Resource<SaveFoodOrderResponse>>() }

    fun getFoodCartObserver(): LiveData<Resource<FoodCartResponse>> = foodCartObserver
    fun getSaveCartObserver(): LiveData<Resource<SaveFoodOrderResponse>> = saveCartObserver

    fun getFoodCart(cartRequest: CartRequest){
        launch {
            foodCartObserver.value = Resource.loading()
            val response = userRepository.getFoodCart(cartRequest)
            if (response.isSuccess()){
                cartHandler.changeSavedFoodCart(cartRequest.itemList)
            }
            foodCartObserver.value = response
        }
    }

    fun saveFoodCart(cartRequest: CartRequest){
        launch {
            saveCartObserver.value = Resource.loading()
            val response = userRepository.saveFoodOrder(cartRequest)
            if (response.isSuccess()){
                cartHandler.emptySavedCart(AppConstants.CART_TYPE_FOOD)
            }
            saveCartObserver.value = response
        }
    }
}