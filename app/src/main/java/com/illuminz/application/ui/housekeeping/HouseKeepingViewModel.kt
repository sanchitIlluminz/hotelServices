package com.illuminz.application.ui.housekeeping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.ui.base.BaseViewModel
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HouseKeepingViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel() {

    private val houseKeepingObserver by lazy { MutableLiveData<Resource<List<ServiceCategoryItemDto>>>() }

    fun getHouseObserver():LiveData<Resource<List<ServiceCategoryItemDto>>> = houseKeepingObserver

    fun getHouseKeeping(serviceId: String, serviceTag: String){
        launch {
            houseKeepingObserver.value = Resource.loading()
            val response = userRepository.getServiceProduct(serviceId, serviceTag)
            houseKeepingObserver.value = response
        }
    }
}