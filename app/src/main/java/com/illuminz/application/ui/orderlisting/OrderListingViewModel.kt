package com.illuminz.application.ui.orderlisting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.extensions.orZero
import com.core.ui.base.BaseViewModel
import com.illuminz.application.ui.home.RoomDetailsHandler
import com.illuminz.data.models.common.PagingResult
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.request.OrderListingRequest
import com.illuminz.data.models.response.OrderListingResponse
import com.illuminz.data.models.response.ServiceRequestResponse
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderListingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val roomDetailsHandler: RoomDetailsHandler
) : BaseViewModel() {
    private var getOrdersJob: Job? = null
    private var hasNext = false
    private var pageNo = 1
    private var recordsPerPage = -1
    private var totalRecords = 0
    private var recordsLoaded = 0

    private val ordersObserver by lazy {
        MutableLiveData<Resource<PagingResult<OrderListingResponse>>>()
    }

    private val serviceRequestObserver by lazy {
        MutableLiveData<Resource<ServiceRequestResponse>>()
    }

    fun getOrdersObserver(): LiveData<Resource<PagingResult<OrderListingResponse>>> =
        ordersObserver

    fun getServiceResponseObserver(): LiveData<Resource<ServiceRequestResponse>> =
        serviceRequestObserver

    fun getOrdersListing(resetPage: Boolean = false, orderType: Int) {
        if (ordersObserver.value!=null){
            ordersObserver.value = Resource.success(ordersObserver.value?.data)
        }else{
            getOrdersJob?.cancel()

            getOrdersJob = launch {
                val isFirstPage = pageNo == 1
                ordersObserver.value = Resource.loading(
                    PagingResult(isFirstPage)
                )

                val roomDetailHandler = getRoomHandler()
                val roomNo = roomDetailHandler.roomDetails.roomNo
                val groupCode = roomDetailHandler.roomDetails.groupCode

                val request = OrderListingRequest(
                    room = roomNo,
                    groupCode = groupCode,
                    orderType = orderType,
                    status = -1,
                    page = pageNo
                )
                val resource = userRepository.getOrderListing(request)
                if (resource.status == Status.SUCCESS) {
                    resource.data?.let {
                        totalRecords = it.totalRecords.orZero()
                        pageNo = it.page.orZero()
                        recordsPerPage = it.perPage.orZero()
                        recordsLoaded += it.data?.size.orZero()
                    }
                    if (recordsLoaded<totalRecords){
                        setHasNext(true)
                    }else{
                        setHasNext(false)
                    }

                    val result = PagingResult(isFirstPage, resource.data)
                    ordersObserver.value = Resource.success(result)
                } else {
                    ordersObserver.value =
                        Resource.error(
                            resource.error,
                            PagingResult(isFirstPage)
                        )
                }
        }


//            if (resetPage) {
//                pageNo = 1
//                setHasNext(false)
//            }
//            val isFirstPage = pageNo == 1
//            ordersObserver.value = Resource.loading(
//                PagingResult(isFirstPage)
//            )
//            val request = OrderListingRequest(room = 111,
//                groupCode = "111",
//                orderType = orderType,
//                status = -1,
//                page = pageNo)
//            val resource = userRepository.getOrderListing(request)
//            if (resource.status == Status.SUCCESS) {
//                if (resource.data?.page == 0 || resource.data?.page == pageNo) {
//                    setHasNext(false)
//                } else {
//                    setHasNext(true)
//                }
//                resource.data?.let {
//                    totalRecords = it.totalRecords.orZero()
//                    pageNo = it.page.orZero()
//                    recordsPerPage = it.perPage.orZero()
//                }
//                val result = PagingResult(isFirstPage, resource.data)
//                ordersObserver.value = Resource.success(result)
//            } else {
//                ordersObserver.value =
//                    Resource.error(
//                        resource.error,
//                        PagingResult(isFirstPage)
//                    )
//            }
        }
    }

    private fun setHasNext(value: Boolean) {
        hasNext = value
//        if (hasNext) {
//            pageNo += 1
//        }
    }

    fun getServiceRequest(roomNumber: Int, groupCode: String, requestType:Int){
        launch {
            serviceRequestObserver.value = Resource.loading()
            val response = userRepository.getServiceRequest(roomNumber, groupCode, requestType)
            serviceRequestObserver.value = response
        }
    }

    fun isValidForPaging(): Boolean =
        ordersObserver.value?.status != Status.LOADING && hasNext

    fun getRoomHandler(): RoomDetailsHandler = roomDetailsHandler

}