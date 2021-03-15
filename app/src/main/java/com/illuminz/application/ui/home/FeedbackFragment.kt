package com.illuminz.application.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.core.extensions.supportsChangeAnimations
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.home.items.FeedbackRatingItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_feedback.*

class FeedbackFragment : DaggerBaseFragment() {

    companion object {
        const val TAG = "FeedbackFragment"

        fun newInstance():FeedbackFragment{
            return FeedbackFragment()
        }
    }

    private lateinit var ratingAdapter: GroupAdapter<GroupieViewHolder>
    private var selectedFeedbackRatingItem: FeedbackRatingItem? = null

    override fun getLayoutResId(): Int = R.layout.fragment_feedback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        ratingAdapter.setOnItemClickListener { item, view ->

            if (item is FeedbackRatingItem && item != selectedFeedbackRatingItem && selectedFeedbackRatingItem !=null){
                item.selected = true
                item.notifyChanged()

                selectedFeedbackRatingItem?.selected = false
                selectedFeedbackRatingItem?.notifyChanged()
                selectedFeedbackRatingItem = item
            }else if(item is FeedbackRatingItem && selectedFeedbackRatingItem ==null){
                item.selected = true
                item.notifyChanged()
                selectedFeedbackRatingItem = item

            }
        }

        btSubmitFeedback.setOnClickListener {
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun initialise() {
        ratingAdapter = GroupAdapter()
        rvFeedback.adapter = ratingAdapter
        rvFeedback.supportsChangeAnimations(false)

        val item = listOf(      FeedbackRatingItem(rating = 1),
                                FeedbackRatingItem(rating = 2),
                                FeedbackRatingItem(rating = 3),
                                FeedbackRatingItem(rating = 4),
                                FeedbackRatingItem(rating = 5)
        )

        ratingAdapter.addAll(item)

    }
}