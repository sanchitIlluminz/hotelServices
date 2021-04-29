package com.illuminz.application.ui.roomcleaning

import androidx.lifecycle.LiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.AppConstants
import com.core.utils.SingleLiveEvent
import com.illuminz.application.ui.home.RoomDetailsHandler
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.ServiceRequest
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomCleaningViewModel @Inject constructor(
    val userRepository: UserRepository,
    private val roomDetailsHandler: RoomDetailsHandler
): BaseViewModel() {
    private val roomCleaningObserver by lazy { SingleLiveEvent<Resource<Any>>() }

    fun getRoomObserver() : LiveData<Resource<Any>> = roomCleaningObserver

    fun submitCleaningRequest(){
        launch {
            roomCleaningObserver.value = Resource.loading()

            val roomDetailHandler = getRoomHandler()
            val roomNo = roomDetailHandler.roomDetails.roomNo
            val groupCode = roomDetailHandler.roomDetails.groupCode
            val serviceRequest = ServiceRequest(
                roomNumber = roomNo,
                groupCode = groupCode,
                requestType = AppConstants.SERVICE_REQUEST_TYPE_ROOM_CLEANING,
                detail = "Room cleaning"
            )
            roomCleaningObserver.value = userRepository.submitServiceRequest(serviceRequest)
        }
    }

    fun getRoomHandler(): RoomDetailsHandler = roomDetailsHandler

}