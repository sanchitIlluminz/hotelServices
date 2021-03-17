package com.illuminz.application.ui.laundry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.extensions.isNullOrZero
import com.core.extensions.orZero
import com.core.ui.base.BaseViewModel
import com.core.utils.AppConstants
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.ServiceCategoryDto
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LaundryViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val laundryObserver by lazy { MutableLiveData<Resource<Any>>() }
    private val searchItemObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryItemDto>>>() }
    private val finalPriceObserver by lazy { SingleLiveEvent<Resource<List<Double>>>() }
    private val finalItemNumberObserver by lazy { SingleLiveEvent<Int>() }

    private var onlyIronList = mutableListOf<ServiceCategoryDto>()
    private var washIronList = mutableListOf<ServiceCategoryDto>()
    private var onlyIronCartList = mutableListOf<ServiceCategoryDto>()
    private var washIronCartList = mutableListOf<ServiceCategoryDto>()
    private var finalCartList = mutableListOf<ServiceCategoryDto>()

    fun getLaundaryObserver(): MutableLiveData<Resource<Any>> = laundryObserver
    fun getPriceObserver(): LiveData<Resource<List<Double>>> = finalPriceObserver

    fun getLaundryDetails(id: String, tag: String, laundryType: String) {
        launch {
            laundryObserver.value = Resource.loading()
            val resource = userRepository.getServiceProduct(id, tag)

            if (laundryType == AppConstants.LAUNDARY_ONLY_IRON) {
                onlyIronList.clear()
                resource.data?.forEach { serviceCategory ->
                    if (!serviceCategory.ironingPrice.isNullOrZero()) {
                        val itemDto = ServiceCategoryDto(
                            id = serviceCategory.id,
                            itemName = serviceCategory.itemName,
                            ironingPrice = serviceCategory.ironingPrice,
                            quantity = serviceCategory.quantity
                        )
                        onlyIronList.add(itemDto)
                    }
                }
            } else {
                washIronList.clear()
                resource.data?.forEach { serviceCategory ->
                    if (!serviceCategory.washIroningPrice.isNullOrZero()) {
                        val itemDto = ServiceCategoryDto(
                            id = serviceCategory.id,
                            itemName = serviceCategory.itemName,
                            washIroningPrice = serviceCategory.washIroningPrice,
                            quantity = serviceCategory.quantity
                        )
                        washIronList.add(itemDto)
                    }
                }
            }
            laundryObserver.value = resource
        }
    }

    fun updateCartItems(cartList: MutableList<ServiceCategoryDto>, laundryType: String) {
        var price = 0.0
        var items = 0
        if (laundryType == AppConstants.LAUNDARY_ONLY_IRON) {
            onlyIronCartList.clear()
            onlyIronCartList.addAll(cartList)
        } else {
            washIronCartList.clear()
            washIronCartList.addAll(cartList)
        }

        onlyIronCartList.forEach {
            price += it.ironingPrice.orZero() * it.quantity.orZero()
            items += it.quantity.orZero()
        }

        washIronCartList.forEach {
            price += it.washIroningPrice.orZero() * it.quantity.orZero()
            items += it.quantity.orZero()
        }
//        finalCartList.clear()
//        finalCartList.addAll(onlyIronCartList)
//        finalCartList.addAll(onlyIronCartList)
//        finalCartObserver.value = finalCartList
        finalPriceObserver.value =
            Resource.success(listOf(price.orZero(), items.orZero().toDouble()))
    }

    fun getOnlyIronList(): MutableList<ServiceCategoryDto> = onlyIronList
    fun getwashIronList(): MutableList<ServiceCategoryDto> = washIronList
}