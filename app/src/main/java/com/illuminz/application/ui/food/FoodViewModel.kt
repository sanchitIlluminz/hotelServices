package com.illuminz.application.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.AppConstants
import com.core.utils.SingleLiveEvent
import com.illuminz.application.ui.cart.CartHandler
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.CartItemDetail
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.models.response.ServiceCategoryDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class FoodViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val cartHandler: CartHandler
) : BaseViewModel() {
    private val foodProductsObserver by lazy { MutableLiveData<Resource<List<ServiceCategoryDto>>>() }
    private val searchItemObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryItemDto>>>() }

    private var searchProductsJob: Job? = null

    private val foodList = mutableListOf<ServiceCategoryItemDto>()

    fun getFoodProductObserver(): LiveData<Resource<List<ServiceCategoryDto>>> = foodProductsObserver
    fun getSearchItemsObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> = searchItemObserver



    fun getFoodProducts(id: String, tag: String) {
       if (foodProductsObserver.value == null){
           launch {
               foodList.clear()
               foodProductsObserver.value = Resource.loading()
               val response = userRepository.getFoodProduct(id, tag)
               if (response.isSuccess()) {
                   response.data?.forEach { service ->
                       foodList.addAll(service.itemsArr.orEmpty())
                   }
               }
               foodProductsObserver.value = response
           }
       } else{
           //For back button case
           //Here quantity is set 0 so that original list is returned which is updated by cart handler
           foodProductsObserver.value?.data?.forEach { serviceCategory->
               serviceCategory.itemsArr?.forEach { serviceCategoryItem->
                   serviceCategoryItem.quantity = 0
               }
           }
           foodProductsObserver.value = Resource.success(foodProductsObserver.value?.data)
       }
    }

    fun searchItems(query: String?) {
        searchProductsJob?.cancel()
        if (query.isNullOrBlank()) {
            searchItemObserver.value = Resource.success()
            return
        }
        searchProductsJob = launch {
            withContext(Dispatchers.Default) {
                val searchTextLowerCase = query.toLowerCase(Locale.US)
                val searchedFoodItems = foodList.filter { food ->
                    food.itemName?.toLowerCase(Locale.US).orEmpty().contains(searchTextLowerCase)
                }
                searchItemObserver.postValue(Resource.success(searchedFoodItems))
            }
        }
    }

    fun updateFoodList(savedCategoryList: List<ServiceCategoryItemDto>) {
        cartHandler.updateFoodList(savedCategoryList)
    }


    fun addSavedCart(list: List<CartItemDetail>){
        cartHandler.addSavedCart(list,AppConstants.CART_TYPE_FOOD)
    }

    fun getSavedCartList(): List<CartItemDetail>? {
        return cartHandler.getCartList(AppConstants.CART_TYPE_FOOD)
    }

}