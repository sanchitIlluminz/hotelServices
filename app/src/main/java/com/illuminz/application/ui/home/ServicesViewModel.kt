package com.illuminz.application.ui.home

import com.core.ui.base.BaseViewModel
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.ServiceDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServicesViewModel @Inject constructor(
    private val userRepository: UserRepository
) :BaseViewModel(){

    private val servicesObserver by lazy { SingleLiveEvent<Resource<List<ServiceDto>>>() }
    private lateinit var serviceList : List<ServiceDto>

    fun getServices(){
        launch {
            servicesObserver.value = Resource.loading()
            val resource = userRepository.getServices()
            servicesObserver.value = if (resource.isSuccess()){
                resource.data?.let { serviceList = it }
                Resource.success(resource.data)
            }else{
                Resource.error(resource.error)
            }
        }
    }

    fun getServiceList():List<ServiceDto> { return serviceList}

    fun serviceListAvailable():Boolean{
        return this::serviceList.isInitialized
    }

    fun getServiceObserver(): SingleLiveEvent<Resource<List<ServiceDto>>> = servicesObserver
}