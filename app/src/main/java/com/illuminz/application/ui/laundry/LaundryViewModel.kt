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
    private val amountObserver by lazy { MutableLiveData<Resource<List<Double>>>() }

    private var originalOnlyIronList = mutableListOf<ServiceCategoryItemDto>()
    private var originalWashIronList = mutableListOf<ServiceCategoryItemDto>()
    private var onlyIronCartList = mutableListOf<ServiceCategoryItemDto>()
    private var washIronCartList = mutableListOf<ServiceCategoryItemDto>()
    private var cartList = mutableListOf<ServiceCategoryItemDto>()

    private var searchProductJob: Job? = null

    fun getLaundaryObserver(): LiveData<Resource<Any>> = laundryObserver
    fun getAmountObserver(): LiveData<Resource<List<Double>>> = amountObserver
    fun getSearchObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> = searchItemObserver

    fun getLaundryDetails(serviceId: String, serviceTag: String, laundryType: Int) {
        //Check details for which laundry type is needed
        if (laundryType == AppConstants.LAUNDRY_ONLY_IRON) {

            //Check if case of back button
            if (originalOnlyIronList.size == 0) {
                launch {
                    laundryObserver.value = Resource.loading()
                    val response = userRepository.getServiceProduct(serviceId, serviceTag)
                    originalOnlyIronList.clear()

                    response.data?.forEach { serviceCategoryItem ->
                        if (serviceCategoryItem.ironingPrice != null) {
                            val itemDto = ServiceCategoryItemDto(
                                id = serviceCategoryItem.id,
                                itemName = serviceCategoryItem.itemName,
                                ironingPrice = serviceCategoryItem.ironingPrice,
                                quantity = serviceCategoryItem.quantity,
                                laundryType = AppConstants.LAUNDRY_ONLY_IRON
                            )
                            originalOnlyIronList.add(itemDto)
                        }
                    }
                    response.data?.let { initialiseCartList(it) }
                    updateOriginalList(AppConstants.LAUNDRY_ONLY_IRON)
                    updateAllCartLists()
                    laundryObserver.value = response
                    updateLaundryPrice()
                }
            } else {    //Case of back button
                updateOriginalList(AppConstants.LAUNDRY_ONLY_IRON)
                updateAllCartLists()
                laundryObserver.value = Resource.success()
                updateLaundryPrice()
            }
        } else {    //Case of both iron & wash

            //Check if case of back button
            if (originalWashIronList.size == 0) {
                launch {
                    laundryObserver.value = Resource.loading()
                    val response = userRepository.getServiceProduct(serviceId, serviceTag)
                    originalWashIronList.clear()

                    response.data?.forEach { serviceCategoryItem ->
                        if (serviceCategoryItem.washIroningPrice != null) {
                            val itemDto = ServiceCategoryItemDto(
                                id = serviceCategoryItem.id,
                                itemName = serviceCategoryItem.itemName,
                                washIroningPrice = serviceCategoryItem.washIroningPrice,
                                quantity = serviceCategoryItem.quantity,
                                laundryType = AppConstants.LAUNDRY_WASH_IRON
                            )
                            originalWashIronList.add(itemDto)
                        }
                    }
                    response.data?.let { initialiseCartList(it) }
                    updateOriginalList(AppConstants.LAUNDRY_WASH_IRON)
                    updateAllCartLists()
                    laundryObserver.value = response
                    updateLaundryPrice()
                }
            } else {       //Case of back button
                updateOriginalList(AppConstants.LAUNDRY_WASH_IRON)
                updateAllCartLists()
                laundryObserver.value = Resource.success()
                updateLaundryPrice()
            }
        }
    }


    fun getOnlyIronList(): MutableList<ServiceCategoryItemDto> = originalOnlyIronList
    fun getwashIronList(): MutableList<ServiceCategoryItemDto> = originalWashIronList

    fun searchItems(query: String?, laundryType: Int) {
        searchProductJob?.cancel()
        if (query.isNullOrBlank()) {
            searchItemObserver.value = Resource.success()
            return
        }
        searchProductJob = launch {
            withContext(Dispatchers.Default) {
                val searchTextLowerCase = query.toLowerCase(Locale.US)
                val allItemList = mutableListOf<ServiceCategoryItemDto>()

                if (laundryType == AppConstants.LAUNDRY_ONLY_IRON) {
                    allItemList.addAll(originalOnlyIronList)
                } else {
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
    }

    fun updateCartList(cartList: MutableList<ServiceCategoryItemDto>, laundryType: Int) {
        //Update cartItems
        var price = 0.0
        var items = 0

        if (laundryType == AppConstants.LAUNDRY_ONLY_IRON) {
            onlyIronCartList.clear()
            onlyIronCartList.addAll(cartList)
        } else {
            washIronCartList.clear()
            washIronCartList.addAll(cartList)
        }

        onlyIronCartList.forEach { serviceCategoryItem ->
            price += serviceCategoryItem.ironingPrice.orZero() * serviceCategoryItem.quantity.orZero()
            items += serviceCategoryItem.quantity.orZero()
        }

        washIronCartList.forEach { serviceCategoryItem ->
            price += serviceCategoryItem.washIroningPrice.orZero() * serviceCategoryItem.quantity.orZero()
            items += serviceCategoryItem.quantity.orZero()
        }

        amountObserver.value =
            Resource.success(listOf(price.orZero(), items.orZero().toDouble()))
    }

    private fun updateLaundryPrice() {
        var price = 0.0
        var items = 0

        onlyIronCartList.forEach { serviceCategoryItem ->
            price += serviceCategoryItem.ironingPrice.orZero() * serviceCategoryItem.quantity.orZero()
            items += serviceCategoryItem.quantity.orZero()
        }

        washIronCartList.forEach { serviceCategoryItem ->
            price += serviceCategoryItem.washIroningPrice.orZero() * serviceCategoryItem.quantity.orZero()
            items += serviceCategoryItem.quantity.orZero()
        }

        amountObserver.value =
            Resource.success(listOf(price.orZero(), items.orZero().toDouble()))
    }

    fun getFinalCartList(): MutableList<ServiceCategoryItemDto> {
        cartList.clear()
        cartList.addAll(onlyIronCartList)
        cartList.addAll(washIronCartList)
        return cartList
    }

    fun addSavedCart(list: List<CartItemDetail>) {
        cartHandler.addSavedCart(list, AppConstants.CART_TYPE_LAUNDRY)
    }

    private fun updateAllCartLists() {
        cartList.clear()

        val savedCartList = getSavedCartList()
        val ironList = savedCartList?.filter { item -> item.type == AppConstants.LAUNDRY_ONLY_IRON }
        val washList = savedCartList?.filter { item -> item.type == AppConstants.LAUNDRY_WASH_IRON }

        if (ironList != null)
            if (ironList.isEmpty()) {
                onlyIronCartList.clear()
            } else {
                onlyIronCartList.removeAll { ironCartItem ->
                    checkIfItemNotPresent(ironCartItem, ironList)
                }
            }

        if (washList != null)
            if (washList.isEmpty()) {
                washIronCartList.clear()
            } else {
                washIronCartList.removeAll { ironWashCartItem ->
                    checkIfItemNotPresent(ironWashCartItem, washList)
                }
            }

        cartList.addAll(onlyIronCartList)
        cartList.addAll(washIronCartList)
    }

    private fun checkIfItemNotPresent(
        cartItem: ServiceCategoryItemDto,
        savedCartList: List<CartItemDetail>
    ): Boolean {
        var check = true

        for (element in savedCartList) {
            if (cartItem.id == element.id) {
                check = false
                break
            } else {
                check = true
            }
        }
        return check
    }

    /**
        Used to update list when back pressed from cart fragment
     */
    private fun updateOriginalList(type: Int) {
        val savedCartList = getSavedCartList()

        val originalList = if (type == AppConstants.LAUNDRY_ONLY_IRON)
            originalOnlyIronList
        else
            originalWashIronList

        //Set quantity of each item to default i.e 0
        originalList.forEach { item ->
            item.quantity = 0
        }

        //Update quantity of items
        savedCartList?.forEach { savedCartItem ->
            originalList.forEach { item ->
                if ((item.id == savedCartItem.id) &&
                    (item.laundryType == savedCartItem.type)
                ) {
                    item.quantity = savedCartItem.quantity.orZero()
                }
            }
        }
    }

    private fun getSavedCartList(): List<CartItemDetail>? {
        return cartHandler.getCartList(AppConstants.CART_TYPE_LAUNDRY)
    }

    fun savedCartEmptyOrNull(): Boolean {
        val savedCartList = getSavedCartList()
        return savedCartList?.isEmpty() ?: true
    }


    /***
     *  this method is used to add items to cart lists when savedCart has items
     *  not used in case of back pressed
     *  list - list containing all laundry items
     */

    private fun initialiseCartList(list: List<ServiceCategoryItemDto>) {
        if (!savedCartEmptyOrNull()) {  //Check if savedCart has items
            onlyIronCartList.clear()
            washIronCartList.clear()

            val savedList = getSavedCartList()

            //Add saved items to individual cart lists of only iron and wash&iron
            savedList?.forEach { savedItem ->
                list.forEach { item ->
                    if ((item.id == savedItem.id) && (savedItem.type == AppConstants.LAUNDRY_ONLY_IRON)) {
                        val quantity = savedItem.quantity.orZero()
                        val cartItem = ServiceCategoryItemDto(
                            id = item.id,
                            ironingPrice = item.ironingPrice,
                            quantity = quantity,
                            itemName = item.itemName,
                            laundryType = AppConstants.LAUNDRY_ONLY_IRON
                        )
                        onlyIronCartList.add(cartItem)
                    }
                }
                list.forEach { item ->
                    if ((item.id == savedItem.id) && (savedItem.type == AppConstants.LAUNDRY_WASH_IRON)) {
                        val quantity = savedItem.quantity.orZero()
                        val cartItem = ServiceCategoryItemDto(
                            id = item.id,
                            washIroningPrice = item.washIroningPrice,
                            quantity = quantity,
                            itemName = item.itemName,
                            laundryType = AppConstants.LAUNDRY_WASH_IRON
                        )
                        washIronCartList.add(cartItem)
                    }
                }

            }
            updateLaundryPrice()
        }
    }

    fun getOnlyIronCartList(): MutableList<ServiceCategoryItemDto> {
        return onlyIronCartList
    }

    fun getWashIronCartList(): MutableList<ServiceCategoryItemDto> {
        return washIronCartList
    }
}