package com.illuminz.application.ui.nearbyplaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.AppConstants
import com.core.utils.SingleLiveEvent
import com.illuminz.application.ui.home.RoomDetailsHandler
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.ServiceRequest
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NearbyViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val roomDetailsHandler: RoomDetailsHandler
):BaseViewModel() {

    private val nearbyPlacesObserver by lazy { MutableLiveData<Resource<List<ServiceCategoryItemDto>>>() }
    private val cabObserver by lazy { SingleLiveEvent<Resource<Any>>() }

    private val nearbyPlacesList = mutableListOf<ServiceCategoryItemDto>()

    fun getNearbyObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> = nearbyPlacesObserver
    fun getCabObserver() : LiveData<Resource<Any>> = cabObserver

//    fun getNearbyPlaces(serviceId: String, serviceTag: String){
//        launch {
//            nearbyPlacesObserver.value = Resource.loading()
//            val response = userRepository.getServiceProduct(serviceId,serviceTag)
//            if (response.isSuccess()){
//                response.data?.let { nearbyPlacesList.addAll(it) }
//            }
//            nearbyPlacesObserver.value = response
//        }
//    }

    fun getNearbyPlaces(){
        launch {
            nearbyPlacesObserver.value = Resource.loading()
            val response = userRepository.getNearbyPlacesDetail()
            if (response.isSuccess()){
                response.data?.let { nearbyPlacesList.addAll(it) }
            }
            nearbyPlacesObserver.value = response
        }
    }

    fun nearbyListEmpty():Boolean{
        return nearbyPlacesList.size==0
    }

    fun submitCabRequest(roomNumber:Int, groupCode: String, destination:String){
        launch {
            cabObserver.value = Resource.loading()

            val serviceRequest = ServiceRequest(
                roomNumber = roomNumber,
                groupCode = groupCode,
                requestType = AppConstants.SERVICE_REQUEST_TYPE_CAB,
                detail = destination
            )
            cabObserver.value = userRepository.submitServiceRequest(serviceRequest)
        }
    }

    fun getRoomHandler(): RoomDetailsHandler = roomDetailsHandler

}