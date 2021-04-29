package com.illuminz.application.ui.cart

import com.core.extensions.orZero
import com.core.utils.AppConstants
import com.illuminz.data.models.request.CartItemDetail
import com.illuminz.data.models.response.ServiceCategoryItemDto
import javax.inject.Inject

class CartHandler @Inject constructor() {
    private var savedFoodCart: SavedCart? = null
    private var savedLaundryCart: SavedCart? = null

    fun addSavedCart(list: List<CartItemDetail>, type: Int) {
        if (type == AppConstants.CART_TYPE_FOOD)
            savedFoodCart = SavedCart(items = list.toMutableList())
        else
            savedLaundryCart = SavedCart(items = list.toMutableList())
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
        if (newCartList != null) {
            savedFoodCart?.items?.clear()
            savedFoodCart?.items?.addAll(newCartList)
        }
    }

    fun updateSavedLaundryCart(
//        currentCartList: List<CartItemDetail>?
        laundryItem: ServiceCategoryItemDto
//        laundryType:Int
    ) {
        if (savedLaundryCart?.items == null) {
            val list = mutableListOf<CartItemDetail>()
            list.add(
                CartItemDetail(
                    id = laundryItem.id,
                    quantity = laundryItem.quantity,
                    type = laundryItem.laundryType
                )
            )
            savedLaundryCart = SavedCart(items = list.toMutableList())
        } else {

            val savedCartList = savedLaundryCart?.items
            //Check if item is already present in list or not
            val newItem = savedCartList?.find { cartItem ->
                ((cartItem.id == laundryItem.id) && (cartItem.type == laundryItem.laundryType))
            }

            when (newItem) {
                null -> {
                    // Add item to list

                    savedCartList?.add(
                        CartItemDetail(
                            id = laundryItem.id,
                            quantity = laundryItem.quantity,
                            type = laundryItem.laundryType
                        )
                    )
                }
                else -> {
                    // Changes to item in list
                    for (i in 0 until savedCartList.size) {
                        if ((savedCartList[i].id == laundryItem.id) && (savedCartList[i].type == laundryItem.laundryType)) {
                            //Remove item if quantity is zero else update quantity
                            if (laundryItem.quantity == 0) {
                                savedCartList.removeAt(i)
                                break
                            } else {
                                savedCartList[i].quantity = laundryItem.quantity
                            }
                        }
                    }
                }
            }

            savedLaundryCart?.items?.removeAll { cartItem ->
                cartItem.quantity == 0
            }
        }
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