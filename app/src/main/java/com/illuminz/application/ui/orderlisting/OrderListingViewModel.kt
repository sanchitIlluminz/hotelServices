package com.illuminz.application.ui.orderlisting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.ui.base.BaseViewModel
import com.illuminz.data.models.common.PagingResult
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.request.OrderListingRequest
import com.illuminz.data.models.response.OrderListingResponse
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderListingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel(){
    private var getOrdersJob: Job? = null
    private var hasNext = false
    private var pageNo = 1

    private val ordersObserver by lazy {
        MutableLiveData<Resource<PagingResult<OrderListingResponse>>>()
    }

    fun getWalletObserver(): LiveData<Resource<PagingResult<OrderListingResponse>>> =
        ordersObserver

    fun getOrdersListing(resetPage: Boolean = false) {
        getOrdersJob?.cancel()
        getOrdersJob = launch {
            if (resetPage) {
                pageNo = 1
                setHasNext(false)
            }
            val isFirstPage = pageNo == 1
            ordersObserver.value = Resource.loading(
                PagingResult(isFirstPage)
            )
            val request = OrderListingRequest(room = 111,
                groupCode = "111",
                orderType = 0,
                status = -1,
                page = pageNo)
            val resource = userRepository.getOrderListing(request)
            if (resource.status == Status.SUCCESS) {
                if (resource.data?.page == 0 || resource.data?.page == pageNo) {
                    setHasNext(false)
                } else {
                    setHasNext(true)
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
    }

    private fun setHasNext(value: Boolean) {
        hasNext = value
        if (hasNext) {
            pageNo += 1
        }
    }

    fun isValidForPaging(): Boolean =
        ordersObserver.value?.status != Status.LOADING && hasNext
}