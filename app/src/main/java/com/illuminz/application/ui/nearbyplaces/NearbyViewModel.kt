package com.illuminz.application.ui.nearbyplaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NearbyViewModel @Inject constructor(
    private val userRepository: UserRepository
):BaseViewModel() {

    private val nearbyPlacesObserver by lazy { MutableLiveData<Resource<List<ServiceCategoryItemDto>>>() }

    private val nearbyPlacesList = mutableListOf<ServiceCategoryItemDto>()

    fun getNearbyObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> = nearbyPlacesObserver

    fun getNearbyPlaces(serviceId: String, serviceTag: String){
        launch {
            nearbyPlacesObserver.value = Resource.loading()
            val response = userRepository.getServiceProduct(serviceId,serviceTag)
            if (response.isSuccess()){
                response.data?.let { nearbyPlacesList.addAll(it) }
            }
            nearbyPlacesObserver.value = response
        }
    }

    fun nearbyListEmpty():Boolean{
        return nearbyPlacesList.size==0
    }
}