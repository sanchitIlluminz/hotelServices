package com.illuminz.application.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.ui.base.BaseViewModel
import com.core.utils.SingleLiveEvent
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.request.FeedbackRequest
import com.illuminz.data.models.response.FeedbackResponse
import com.illuminz.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedbackViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val roomDetailsHandler: RoomDetailsHandler
): BaseViewModel() {
    private val submitFeedbackObserver by lazy { SingleLiveEvent<Resource<FeedbackResponse>>()}
    private val feedbackObserver by lazy { MutableLiveData<Resource<FeedbackResponse>>()}

    fun getSubmittedFeedbackObserver() : LiveData<Resource<FeedbackResponse>>  = submitFeedbackObserver
    fun getFeedbackObserver() : LiveData<Resource<FeedbackResponse>>  = feedbackObserver

    fun getFeedback(room: Int, groupCode: String){
        launch {
            feedbackObserver.value = Resource.loading()
            val resource = userRepository.getUserFeedback(room, groupCode)
            feedbackObserver.value = resource
        }
    }

    fun submitFeedback(feedbackRequest: FeedbackRequest){
        launch {
            submitFeedbackObserver.value = Resource.loading()
            val resource = userRepository.submitFeedback(feedbackRequest)
            submitFeedbackObserver.value = resource
        }
    }

    fun getRoomHandler(): RoomDetailsHandler = roomDetailsHandler
}