package com.illuminz.application.ui.massage

import com.core.ui.base.BaseViewModel
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.models.response.ServiceCategoryDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch

class MassageViewModel(
   val userRepository: UserRepository
): BaseViewModel() {

    private val massageObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryDto>>>() }
    private var massageList = mutableListOf<ServiceCategoryItemDto>()

    fun getMassageList(id:String,tag:String){
        launch {
            massageObserver.value = Resource.loading()
            val response = userRepository.getServiceProduct(id, tag)
            massageObserver.value = if (response.isSuccess()){
                response.data?.let { addToList(it) }
                Resource.success(response.data)
            }else{
                Resource.error(response.error)
            }
        }
    }

    private fun addToList(list: List<ServiceCategoryDto>){
        list.forEach {
            it.itemsArr?.forEach { foodDto ->
                massageList.add(foodDto)
            }
        }
    }

    fun massageListObserver(): SingleLiveEvent<Resource<List<ServiceCategoryDto>>> = massageObserver
}