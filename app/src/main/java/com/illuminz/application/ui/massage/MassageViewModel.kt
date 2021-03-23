package com.illuminz.application.ui.massage

import androidx.lifecycle.LiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class MassageViewModel @Inject constructor(
   val userRepository: UserRepository
): BaseViewModel() {

    private val massageListObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryItemDto>>>() }
    private val searchItemObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryItemDto>>>() }
    private var massageList = mutableListOf<ServiceCategoryItemDto>()

    private var searchMassageJob: Job? = null

    fun getMassageObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> = massageListObserver
    fun getSearchObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> = searchItemObserver

    fun getMassageList(id:String,tag:String){
        launch {
            massageList.clear()
            massageListObserver.value = Resource.loading()
            val response = userRepository.getServiceProduct(id, tag)

            if(response.isSuccess()){
                response.data?.forEach {
                    massageList.add(it)
                }
            }
            massageListObserver.value = response
        }
    }

    fun updateMassageList(serviceCategoryItemDto: ServiceCategoryItemDto){
        massageList.forEach { massage ->
            if (massage.id == serviceCategoryItemDto.id)
                massage.quantity =serviceCategoryItemDto.quantity
        }
    }

    fun searchItems(query: String?){
        searchMassageJob?.cancel()
        if (query.isNullOrBlank()){
            searchItemObserver.value = Resource.success()
            return
        }
        searchMassageJob = launch {
            withContext(Dispatchers.IO){
                val searchTextLowerCase = query.toLowerCase(Locale.US)
                val searchedItems = massageList.filter { massage ->
                    massage.title?.toLowerCase(Locale.US).orEmpty().contains(searchTextLowerCase)
                }
                searchItemObserver.postValue(Resource.success(searchedItems))
            }
        }
    }
}