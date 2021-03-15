package com.illuminz.application.ui.food

import com.core.ui.base.BaseViewModel
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.FoodDto
import com.illuminz.data.models.response.ServiceProductDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class FoodViewModel @Inject constructor(
    val userRepository: UserRepository
): BaseViewModel() {

    private val foodProductsObserver by lazy { SingleLiveEvent<Resource<List<ServiceProductDto>>>() }
    private val searchItemObserver by lazy { SingleLiveEvent<Resource<List<FoodDto>>>() }
    private var searchProductsJob:Job? = null
    private val foodList = mutableListOf<FoodDto>()

    fun getFoodProducts(id:String,tag:String){
        launch {
            foodProductsObserver.value = Resource.loading()
            val response = userRepository.getServiceProduct(id, tag)
            foodProductsObserver.value = if (response.isSuccess()){
                response.data?.let { addToList(it) }
                Resource.success(response.data)

            }else{
                Resource.error(response.error)
            }
        }
    }

    fun getFoodProductObserver(): SingleLiveEvent<Resource<List<ServiceProductDto>>> = foodProductsObserver

    fun searchItems(query: String?) {
        searchProductsJob?.cancel()
        if (query.isNullOrBlank()) {
            searchItemObserver.value = Resource.success()
            return
        }
        searchProductsJob = launch {
            withContext(Dispatchers.Default) {
                val searchTextLowerCase = query.toLowerCase(Locale.US)
                val searchedFoodItems = foodList.filter { foodDto ->

                    foodDto.name?.toLowerCase(Locale.US).orEmpty().contains(searchTextLowerCase)
                }

                searchItemObserver.postValue(Resource.success(searchedFoodItems))
            }
        }
    }

    fun addToList(list: List<ServiceProductDto>){
        list.forEach {
            it.itemsArr?.forEach { foodDto ->
                foodList.add(foodDto)
            }
        }
    }

    fun getSearchItemsObserver():SingleLiveEvent<Resource<List<FoodDto>>> = searchItemObserver

    fun updateFoodList(foodDto: FoodDto){
        foodList.forEach{
        if (it.id == foodDto.id ){
            it.quantity=foodDto.quantity
        }
        }
    }
}