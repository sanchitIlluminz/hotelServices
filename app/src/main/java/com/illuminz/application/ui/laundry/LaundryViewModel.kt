package com.illuminz.application.ui.laundry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.extensions.orZero
import com.core.ui.base.BaseViewModel
import com.core.utils.AppConstants
import com.core.utils.SingleLiveEvent
import com.illuminz.application.ui.cart.CartHandler
import com.illuminz.application.ui.laundry.items.LaundryItem
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.CartItemDetail
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class LaundryViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val cartHandler: CartHandler
) : BaseViewModel() {
    private val laundryObserver by lazy { MutableLiveData<Resource<Any>>() }
    private val searchItemObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryItemDto>>>() }
    private val amountObserver by lazy { MutableLiveData<Resource<Pair<Int, Double>>>() }

    private var originalList = mutableListOf<ServiceCategoryItemDto>()
    private var originalOnlyIronList = mutableListOf<ServiceCategoryItemDto>()
    private var originalWashIronList = mutableListOf<ServiceCategoryItemDto>()
    private var onlyIronCartList = mutableListOf<ServiceCategoryItemDto>()
    private var washIronCartList = mutableListOf<ServiceCategoryItemDto>()

    private var searchProductJob: Job? = null

    fun getLaundryObserver(): LiveData<Resource<Any>> = laundryObserver
    fun getAmountObserver(): LiveData<Resource<Pair<Int, Double>>> = amountObserver
    fun getSearchObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> = searchItemObserver

    fun getLaundryDetails(serviceId: String, serviceTag: String) {
        launch {
            originalList.clear()
            originalWashIronList.clear()
            originalOnlyIronList.clear()
            laundryObserver.value = Resource.loading()
            val resource = userRepository.getServiceProduct(serviceId, serviceTag)

            originalList.addAll(resource.data.orEmpty())

            originalList.forEach { serviceCategoryItem ->
                val onlyIronItem = serviceCategoryItem.copy(
                    washIroningPrice = null,
                    laundryType = AppConstants.LAUNDRY_ONLY_IRON
                )
                originalOnlyIronList.add(onlyIronItem)

                val washIronItem = serviceCategoryItem.copy(
                    ironingPrice = null,
                    laundryType = AppConstants.LAUNDRY_WASH_IRON
                )
                originalWashIronList.add(washIronItem)
            }
            val savedCartItems = cartHandler.getCartList(AppConstants.CART_TYPE_LAUNDRY)
            if (!savedCartItems.isNullOrEmpty()) {
                updateList(savedCartItems)
            }
            laundryObserver.value = resource
        }
    }

    private fun updateList(savedCartItems: List<CartItemDetail>) {
        originalOnlyIronList.forEach { serviceCategoryItem ->
            savedCartItems.forEach { savedItem ->
                if ((savedItem.id == serviceCategoryItem.id) && (savedItem.type == serviceCategoryItem.laundryType)) {
                    serviceCategoryItem.quantity = savedItem.quantity.orZero()
                }
            }
        }

        originalWashIronList.forEach { serviceCategoryItem ->
            savedCartItems.forEach { savedItem ->
                if ((savedItem.id == serviceCategoryItem.id) && (savedItem.type == serviceCategoryItem.laundryType)) {
                    serviceCategoryItem.quantity = savedItem.quantity.orZero()
                }
            }
        }
    }


    fun getOnlyIronList(): MutableList<ServiceCategoryItemDto> = originalOnlyIronList
    fun getWashIronList(): MutableList<ServiceCategoryItemDto> = originalWashIronList

    fun searchItems(query: String?, laundryType: Int) {
        searchProductJob?.cancel()
        if (query.isNullOrBlank()) {
            searchItemObserver.value = Resource.success()
            return
        }
        searchProductJob = launch {
            withContext(Dispatchers.Default) {
                val searchTextLowerCase = query.toLowerCase(Locale.US)
                val allItemList = if (laundryType == AppConstants.LAUNDRY_ONLY_IRON) {
                    originalOnlyIronList
                } else {
                    originalWashIronList
                }

                val searchedItems = allItemList.filter { laundry ->
                    laundry.itemName?.toLowerCase(Locale.US).orEmpty().contains(searchTextLowerCase)
                }

                searchItemObserver.postValue(Resource.success(searchedItems))
            }
        }
    }

    fun updateOriginalLaundryList(laundryItem: LaundryItem) {
        if (laundryItem.laundryType == AppConstants.LAUNDRY_ONLY_IRON) {
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
        updateLaundryPrice()
        updateCartList(laundryItem.serviceCategoryItem)
    }

    fun updateCartList(laundryItem: ServiceCategoryItemDto) {
        cartHandler.updateSavedLaundryCart(laundryItem)
        updateLaundryPrice()
    }


    fun updateLaundryPrice() {
        var price = 0.0
        var items = 0

        originalOnlyIronList.forEach { serviceCategoryItem ->
            if (serviceCategoryItem.quantity != 0) {
                price += serviceCategoryItem.ironingPrice.orZero() * serviceCategoryItem.quantity.orZero()
                items += serviceCategoryItem.quantity.orZero()
            }
        }

        originalWashIronList.forEach { serviceCategoryItem ->
            if (serviceCategoryItem.quantity != 0) {
                price += serviceCategoryItem.washIroningPrice.orZero() * serviceCategoryItem.quantity.orZero()
                items += serviceCategoryItem.quantity.orZero()
            }

        }

        amountObserver.value =
            Resource.success(items.orZero() to price.orZero())
    }

    fun updateOriginalList() {
        val savedCartItems = cartHandler.getCartList(AppConstants.CART_TYPE_LAUNDRY)
        if (!savedCartItems.isNullOrEmpty()) {
            updateList(savedCartItems)
        }
    }

    fun getSavedCartList(): List<CartItemDetail>? {
        return cartHandler.getCartList(AppConstants.CART_TYPE_LAUNDRY)
    }

    fun savedCartEmptyOrNull(): Boolean {
        val savedCartList = getSavedCartList()
        return savedCartList?.isEmpty() ?: true
    }

    fun getOnlyIronCartList(): MutableList<ServiceCategoryItemDto> {
        return onlyIronCartList
    }

    fun getWashIronCartList(): MutableList<ServiceCategoryItemDto> {
        return washIronCartList
    }
}