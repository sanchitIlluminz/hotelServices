package com.illuminz.application.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.extensions.isNetworkActiveWithMessage
import com.core.extensions.orZero
import com.core.extensions.supportsChangeAnimations
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.home.items.FeedbackRatingItem
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.request.FeedbackRequest
import com.illuminz.data.models.response.FeedbackResponse
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_feedback.*

class FeedbackFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "FeedbackFragment"

        private const val KEY_ROOM_NUMBER = "KEY_ROOM_NUMBER"
        private const val KEY_GROUP_CODE = "KEY_GROUP_CODE"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_CONNECTION_ERROR = 2
        private const val FLIPPER_CHILD_LOADING = 1

        fun newInstance(roomNo: Int, groupCode: String): FeedbackFragment {
            val fragment = FeedbackFragment()
            val arguments = Bundle()
            arguments.putInt(KEY_ROOM_NUMBER, roomNo)
            arguments.putString(KEY_GROUP_CODE, groupCode)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var ratingAdapter: GroupAdapter<GroupieViewHolder>

    private var selectedFeedbackRatingItem: FeedbackRatingItem? = null

    private lateinit var groupCode: String
    private lateinit var feedbackId: String
    private var roomNo = -1

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FeedbackViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_feedback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
        setObservers()
    }

    private fun initialise() {
        roomNo = requireArguments().getInt(KEY_ROOM_NUMBER)
        groupCode = requireArguments().getString(KEY_GROUP_CODE).orEmpty()

        ratingAdapter = GroupAdapter()
        rvFeedback.adapter = ratingAdapter
        rvFeedback.supportsChangeAnimations(false)

        if (requireContext().isNetworkActiveWithMessage()) {
            viewModel.getFeedback(roomNo, groupCode)
        }
//        viewModel.getFeedback(roomNo, groupCode)

    }

    private fun setBasicData(feedbackResponse: FeedbackResponse) {
        val item = listOf(
            FeedbackRatingItem(rating = 1),
            FeedbackRatingItem(rating = 2),
            FeedbackRatingItem(rating = 3),
            FeedbackRatingItem(rating = 4),
            FeedbackRatingItem(rating = 5)
        )

        ratingAdapter.addAll(item)
        val rating = feedbackResponse.rating
        etFeedback.setText(feedbackResponse.comment)
        feedbackId = feedbackResponse.id.orEmpty()

//        selectedFeedbackRatingItem =
//            rating?.let { ratingAdapter.getGroupAtAdapterPosition(it) }
        if (rating != null) {
            if(rating<=5){
                selectedFeedbackRatingItem =  rating?.minus(1).let { ratingAdapter.getItem(it) } as FeedbackRatingItem
                selectedFeedbackRatingItem?.selected = true
                ratingAdapter.notifyItemChanged(rating-1)
            }else{
                selectedFeedbackRatingItem =  ratingAdapter.getItem(4) as FeedbackRatingItem
                selectedFeedbackRatingItem?.selected = true
                ratingAdapter.notifyItemChanged(4)
            }
        }else{
            selectedFeedbackRatingItem =  ratingAdapter.getItem(4)  as FeedbackRatingItem
            selectedFeedbackRatingItem?.selected = true
        }
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        ratingAdapter.setOnItemClickListener { item, view ->
            if (item is FeedbackRatingItem && item != selectedFeedbackRatingItem
                && selectedFeedbackRatingItem != null
            ) {
                item.selected = true
                item.notifyChanged()

                selectedFeedbackRatingItem?.selected = false
                selectedFeedbackRatingItem?.notifyChanged()
                selectedFeedbackRatingItem = item
            }
            else if (item is FeedbackRatingItem && selectedFeedbackRatingItem == null) {
                item.selected = true
                item.notifyChanged()

                selectedFeedbackRatingItem = item
            }
        }

        btSubmitFeedback.setOnClickListener {
            val comment = etFeedback.text.toString()
            val rating = selectedFeedbackRatingItem?.rating
            val id = if (this::feedbackId.isInitialized)
                feedbackId
            else
                ""

            val feedbackRequest = FeedbackRequest(
                room = roomNo,
                groupCode = groupCode,
                comment = comment,
                id = id,
                rating = rating
            )
            if (requireContext().isNetworkActiveWithMessage()) {
                viewModel.submitFeedback(feedbackRequest)
            }
        }
    }


    private fun setObservers() {
        viewModel.getFeedbackObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    resource.data?.let {
                        setBasicData(it)
                    }
                }

                Status.ERROR -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_CONNECTION_ERROR
                    handleError(resource.error)
                }
            }
        })


        viewModel.getSubmittedFeedbackObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showLoading()
                }

                Status.SUCCESS -> {
                    dismissLoading()
                    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }

                Status.ERROR -> {
                    dismissLoading()
                    handleError(resource.error)
                }
            }
        })
    }
}