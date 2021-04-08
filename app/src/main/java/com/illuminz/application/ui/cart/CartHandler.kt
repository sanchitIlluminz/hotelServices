package com.illuminz.application.ui.cart

import com.core.extensions.orZero
import com.illuminz.data.models.request.CartRequest
import com.illuminz.data.models.response.ServiceCategoryItemDto
import javax.inject.Inject

class CartHandler @Inject constructor() {
    private var savedCart: savedCart? = null

    fun addSavedCart(list: List<CartRequest>, tag: String) {
        savedCart = savedCart(type = tag, items = list)
    }

    /**
     * Used to update the saved cart when changes in cartScreen are done
     */
    fun changeSavedCart(currentCartList: List<CartRequest>?) {
        currentCartList?.forEach { currentCartItem ->
            savedCart?.items?.forEach { savedCartItem ->
                if (savedCartItem.id == currentCartItem.id) {
                    savedCartItem.quantity = currentCartItem.quantity
                }
            }
        }
        val newCartList = savedCart?.items?.filter { foodRequest -> foodRequest.quantity != 0 }
        savedCart?.items = newCartList
    }

    /**
     * Used to update the original list
     * and saved cart
     */
    fun updateFoodList(foodList: List<ServiceCategoryItemDto>) {
        foodList.forEach { serviceCategoryItem ->
            savedCart?.items?.forEach { savedCartItem ->
                if (serviceCategoryItem.id == savedCartItem.id) {
                    serviceCategoryItem.quantity = savedCartItem.quantity.orZero()
                }
            }
        }
    }

    fun getCartList(): List<CartRequest>? {
        return savedCart?.items
    }

    fun emptySavedCart() {
        savedCart = null
    }
}