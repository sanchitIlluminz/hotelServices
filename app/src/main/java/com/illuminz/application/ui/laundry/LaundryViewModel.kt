package com.illuminz.application.ui.laundry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.extensions.orZero
import com.core.ui.base.BaseViewModel
import com.core.utils.AppConstants
import com.core.utils.SingleLiveEvent
import com.illuminz.application.ui.laundry.items.LaundryItem
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class LaundryViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val laundryObserver by lazy { MutableLiveData<Resource<Any>>() }
    private val searchItemObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryItemDto>>>() }
    private val finalPriceObserver by lazy { SingleLiveEvent<Resource<List<Double>>>() }

    private var originalOnlyIronList = mutableListOf<ServiceCategoryItemDto>()
    private var originalWashIronList = mutableListOf<ServiceCategoryItemDto>()
    private var onlyIronCartList = mutableListOf<ServiceCategoryItemDto>()
    private var washIronCartList = mutableListOf<ServiceCategoryItemDto>()
    private var finalCartList = mutableListOf<ServiceCategoryItemDto>()

    private var searchProductJob: Job? = null

    fun getLaundaryObserver(): LiveData<Resource<Any>> = laundryObserver
    fun getPriceObserver(): LiveData<Resource<List<Double>>> = finalPriceObserver
    fun getSearchObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> = searchItemObserver

    fun getLaundryDetails(id: String, tag: String, laundryType: String) {
        launch {
            laundryObserver.value = Resource.loading()
            val response = userRepository.getServiceProduct(id, tag)

            if (laundryType == AppConstants.LAUNDARY_ONLY_IRON) {
                originalOnlyIronList.clear()
                response.data?.forEach { serviceCategoryItem ->
                    if (serviceCategoryItem.ironingPrice != null) {
                        val itemDto = ServiceCategoryItemDto(
                            id = serviceCategoryItem.id,
                            itemName = serviceCategoryItem.itemName,
                            ironingPrice = serviceCategoryItem.ironingPrice,
                            quantity = serviceCategoryItem.quantity
                        )
                        originalOnlyIronList.add(itemDto)
                    }
                }
            } else {
                originalWashIronList.clear()
                response.data?.forEach { serviceCategoryItem ->
                    if (serviceCategoryItem.washIroningPrice != null) {
                        val itemDto = ServiceCategoryItemDto(
                            id = serviceCategoryItem.id,
                            itemName = serviceCategoryItem.itemName,
                            washIroningPrice = serviceCategoryItem.washIroningPrice,
                            quantity = serviceCategoryItem.quantity
                        )
                        originalWashIronList.add(itemDto)
                    }
                }
            }
            laundryObserver.value = response
        }
    }


    fun getOnlyIronList(): MutableList<ServiceCategoryItemDto> = originalOnlyIronList
    fun getwashIronList(): MutableList<ServiceCategoryItemDto> = originalWashIronList

    fun searchItems(query: String?,laundryType: String) {
        searchProductJob?.cancel()
        if (query.isNullOrBlank()) {
            searchItemObserver.value = Resource.success()
            return
        }
        searchProductJob = launch {
            withContext(Dispatchers.Default) {
                val searchTextLowerCase = query.toLowerCase(Locale.US)
                val allItemList = mutableListOf<ServiceCategoryItemDto>()
                if (laundryType==AppConstants.LAUNDARY_ONLY_IRON){
                    allItemList.addAll(originalOnlyIronList)
                }else{
                    allItemList.addAll(originalWashIronList)
                }
                val searchedItems = allItemList.filter { laundry ->
                    laundry.itemName?.toLowerCase(Locale.US).orEmpty().contains(searchTextLowerCase)
                }

                searchItemObserver.postValue(Resource.success(searchedItems))
            }
        }
    }

    fun updateOriginalLaundryList(laundryItem: LaundryItem) {
        if (laundryItem.laundryType == AppConstants.LAUNDARY_ONLY_IRON) {
            originalOnlyIronList.forEach { serviceCategoryItem ->
                if (serviceCategoryItem.id == laundryItem.serviceCategoryItem.id) {
                    serviceCategoryItem.quantity = laundryItem.serviceCategoryItem.quantity
                }
            }
        } else {
            originalWashIronList.forEach { serviceCategoryItem ->
                if (serviceCategoryItem.id == laundryItem.serviceCategoryItem.id) {
                    serviceCategoryItem.quantity = laundryItem.serviceCategoryItem.quantity
                }
            }
        }
    }

    fun updateFinalCartList(cartList: MutableList<ServiceCategoryItemDto>, laundryType: String) {
        //Update cartItems
        var price = 0.0
        var items = 0
        if (laundryType == AppConstants.LAUNDARY_ONLY_IRON) {
            onlyIronCartList.clear()
            onlyIronCartList.addAll(cartList)
        } else {
            washIronCartList.clear()
            washIronCartList.addAll(cartList)
        }

        onlyIronCartList.forEach {  serviceCategoryItem ->
            price += serviceCategoryItem.ironingPrice.orZero() * serviceCategoryItem.quantity.orZero()
            items += serviceCategoryItem.quantity.orZero()
        }

        washIronCartList.forEach {  serviceCategoryItem ->
            price += serviceCategoryItem.washIroningPrice.orZero() * serviceCategoryItem.quantity.orZero()
            items += serviceCategoryItem.quantity.orZero()
        }

        finalPriceObserver.value =
            Resource.success(listOf(price.orZero(), items.orZero().toDouble()))
    }

    fun getFinalCartList():MutableList<ServiceCategoryItemDto>{
        finalCartList.clear()
        finalCartList.addAll(onlyIronCartList)
        finalCartList.addAll(washIronCartList)
        return finalCartList
    }
}