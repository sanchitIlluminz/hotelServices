package com.illuminz.application.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.AppConstants
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.ServiceRequest
import com.illuminz.data.models.response.GuestInfoResponse
import com.illuminz.data.models.response.ServiceDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServicesViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val roomDetailsHandler: RoomDetailsHandler
) : BaseViewModel() {

    private val servicesObserver by lazy { MutableLiveData<Resource<Pair<List<ServiceDto>, GuestInfoResponse?>>>() }
    private val cabObserver by lazy { SingleLiveEvent<Resource<Any>>() }

    private lateinit var serviceList: List<ServiceDto>

    fun getServiceObserver(): LiveData<Resource<Pair<List<ServiceDto>, GuestInfoResponse?>>> =
        servicesObserver

    fun getCabObserver() : LiveData<Resource<Any>> = cabObserver

    data class Test(
        val a: List<ServiceDto>?, val b: List<ServiceDto>?
    )

    fun getServices(room: Int, groupCode: String) {
        if (servicesObserver.value==null)
        launch {
            servicesObserver.value = Resource.loading()

            val servicesResource = userRepository.getServices()

            val guestResource = if (servicesResource.isSuccess())
                userRepository.getUserInfo(room, groupCode)
            else
                null

            if (servicesResource.isSuccess() && guestResource?.isSuccess() == true) {
                if (guestResource.data!=null)
                servicesObserver.value =
                    Resource.success(servicesResource.data.orEmpty() to guestResource.data)
            } else {
                val error = if (!servicesResource.isSuccess())
                    servicesResource.error
                else {
                    guestResource?.error
                }
                servicesObserver.value = Resource.error(error)
            }
        }

    }

    fun getServiceList(): List<ServiceDto> {
        return serviceList
    }

    fun serviceListAvailable(): Boolean {
        return this::serviceList.isInitialized
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