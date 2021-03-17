package com.illuminz.application.ui.food

import androidx.lifecycle.LiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
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
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val foodProductsObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryDto>>>() }
    private val searchItemObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryItemDto>>>() }

    private var searchProductsJob: Job? = null

    private val foodList = mutableListOf<ServiceCategoryItemDto>()

    fun getFoodProductObserver(): LiveData<Resource<List<ServiceCategoryDto>>> =
        foodProductsObserver

    fun getSearchItemsObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> =
        searchItemObserver

    fun getFoodProducts(id: String, tag: String) {
        launch {
            foodProductsObserver.value = Resource.loading()
            val resource = userRepository.getServiceProduct(id, tag)
            if (resource.isSuccess()) {
                resource.data?.forEach { service ->
                    foodList.addAll(service.itemsArr.orEmpty())
                }
            }
            foodProductsObserver.value = resource
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
                    food.name?.toLowerCase(Locale.US).orEmpty().contains(searchTextLowerCase)
                }
//                searchItemObserver.value = Resource.success(searchedFoodItems)
                searchItemObserver.postValue(Resource.success(searchedFoodItems))

            }
        }
    }

    fun updateFoodList(serviceCategoryItem: ServiceCategoryItemDto) {
        foodList.forEach {
            if (it.id == serviceCategoryItem.id) {
                it.quantity = serviceCategoryItem.quantity
            }
        }
    }
}