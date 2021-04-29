package com.illuminz.application.ui.gym

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class GymViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel() {
    private val gymObserver by lazy { SingleLiveEvent<Resource<List<ServiceCategoryItemDto>>>() }

    fun getGymObserver(): LiveData<Resource<List<ServiceCategoryItemDto>>> = gymObserver

    fun getGymDetail(){
        launch {
            gymObserver.value = Resource.loading()
            val response = userRepository.getGymDetails()
            gymObserver.value = response
        }
    }
}