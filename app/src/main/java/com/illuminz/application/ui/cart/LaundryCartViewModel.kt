package com.illuminz.application.ui.cart

import androidx.lifecycle.LiveData
import com.core.extensions.orZero
import com.core.ui.base.BaseViewModel
import com.core.utils.AppConstants
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.CartItemDetail
import com.illuminz.data.models.request.CartRequest
import com.illuminz.data.models.response.CartItemDto
import com.illuminz.data.models.response.LaundryCartResponse
import com.illuminz.data.models.response.SaveLaundryOrderResponse
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LaundryCartViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val cartHandler: CartHandler
) : BaseViewModel() {
    private val laundryCartObserver by lazy { SingleLiveEvent<Resource<LaundryCartResponse>>() }
    private val saveCartObserver by lazy { SingleLiveEvent<Resource<SaveLaundryOrderResponse>>() }

    fun getLaundryCartObserver(): LiveData<Resource<LaundryCartResponse>> = laundryCartObserver
    fun getSaveCartObserver(): LiveData<Resource<SaveLaundryOrderResponse>> = saveCartObserver

    fun getLaundryCart(cartRequest: CartRequest) {
        launch {
            laundryCartObserver.value = Resource.loading()
            val response = userRepository.getLaundryCart(cartRequest)
//            if (response.isSuccess()){
//                cartHandler.updateSavedLaundryCart(cartRequest.itemList)
//            }
            laundryCartObserver.value = response
        }
    }

    fun saveLaundryCart(cartRequest: CartRequest) {
        launch {
            saveCartObserver.value = Resource.loading()
            val response = userRepository.saveLaundryOrder(cartRequest)
            if (response.isSuccess()) {
                cartHandler.emptySavedCart(AppConstants.CART_TYPE_LAUNDRY)
            }
            saveCartObserver.value = response
        }
    }

    fun getSavedCart(): List<CartItemDetail>? {
        return cartHandler.getCartList(AppConstants.CART_TYPE_LAUNDRY)
    }

    fun updateCartList(
        laundryItem: CartItemDto
    ) {
        val serviceCategoryItemDto = ServiceCategoryItemDto(
            id = laundryItem.id,
            quantity = laundryItem.quantity.orZero(),
            laundryType = laundryItem.laundryType.orZero()
        )
        cartHandler.updateSavedLaundryCart(serviceCategoryItemDto)
    }
}