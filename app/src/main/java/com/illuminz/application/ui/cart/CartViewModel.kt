package com.illuminz.application.ui.cart

import androidx.lifecycle.LiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.FoodCartRequest
import com.illuminz.data.models.response.FoodCartResponse
import com.illuminz.data.models.response.SaveOrderResponse
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel  @Inject constructor(
    private val userRepository: UserRepository,
    private val cartHandler: CartHandler
) : BaseViewModel() {
    private val foodCartObserver by lazy { SingleLiveEvent<Resource<FoodCartResponse>>() }
    private val saveCartObserver by lazy { SingleLiveEvent<Resource<SaveOrderResponse>>() }

    fun getFoodCartObserver(): LiveData<Resource<FoodCartResponse>> = foodCartObserver
    fun getSaveCartObserver(): LiveData<Resource<SaveOrderResponse>> = saveCartObserver

    fun getFoodCart(foodCartRequest: FoodCartRequest){
        launch {
            foodCartObserver.value = Resource.loading()
            val response = userRepository.getFoodCart(foodCartRequest)
            if (response.isSuccess()){
                cartHandler.changeSavedCart(foodCartRequest.itemList)
            }
            foodCartObserver.value = response

        }
    }

    fun saveFoodCart(foodCartRequest: FoodCartRequest){
        launch {
            saveCartObserver.value = Resource.loading()
            val response = userRepository.saveFoodOrder(foodCartRequest)
            if (response.isSuccess()){
                cartHandler.emptySavedCart()
            }
            saveCartObserver.value = response
        }
    }
}