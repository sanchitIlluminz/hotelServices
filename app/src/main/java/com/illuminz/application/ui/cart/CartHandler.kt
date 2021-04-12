package com.illuminz.application.ui.cart

import com.core.extensions.orZero
import com.core.utils.AppConstants
import com.illuminz.data.models.request.CartItemDetail
import com.illuminz.data.models.response.ServiceCategoryItemDto
import javax.inject.Inject

class CartHandler @Inject constructor() {
    private var savedFoodCart: savedCart? = null
    private var savedLaundryCart: savedCart? = null

    fun addSavedCart(list: List<CartItemDetail>, type: Int) {
        if (type == AppConstants.CART_TYPE_FOOD)
            savedFoodCart = savedCart( items = list)
        else
            savedLaundryCart = savedCart( items = list)
    }

    /**
     * Used to update the saved cart when changes in cartScreen are done
     */
    fun changeSavedFoodCart(currentCartList: List<CartItemDetail>?) {
        currentCartList?.forEach { currentCartItem ->
            savedFoodCart?.items?.forEach { savedCartItem ->
                if (savedCartItem.id == currentCartItem.id) {
                    savedCartItem.quantity = currentCartItem.quantity
                }
            }
        }
        val newCartList = savedFoodCart?.items?.filter { foodRequest -> foodRequest.quantity != 0 }
        savedFoodCart?.items = newCartList
    }

    fun changeSavedLaundryCart(currentCartList: List<CartItemDetail>?) {
        currentCartList?.forEach { currentCartItem ->
            savedLaundryCart?.items?.forEach { savedCartItem ->
                if ((savedCartItem.id == currentCartItem.id) && (savedCartItem.type == currentCartItem.type))  {
                    savedCartItem.quantity = currentCartItem.quantity
                }
            }
        }
        val newCartList = savedLaundryCart?.items?.filter { laundryRequest -> laundryRequest.quantity != 0 }
        savedLaundryCart?.items = newCartList
    }

    /**
     * Used to update the original list
     * and saved cart
     */
    fun updateFoodList(foodList: List<ServiceCategoryItemDto>) {
        foodList.forEach { serviceCategoryItem ->
            savedFoodCart?.items?.forEach { savedCartItem ->
                if (serviceCategoryItem.id == savedCartItem.id) {
                    serviceCategoryItem.quantity = savedCartItem.quantity.orZero()
                }
            }
        }
    }

    fun getCartList(type: Int): List<CartItemDetail>? {
        return if (type == AppConstants.CART_TYPE_FOOD)
            savedFoodCart?.items
        else
            savedLaundryCart?.items
    }

    fun emptySavedCart(type: Int) {
        if (type == AppConstants.CART_TYPE_FOOD)
            savedFoodCart = null
        else
            savedLaundryCart = null
    }
}