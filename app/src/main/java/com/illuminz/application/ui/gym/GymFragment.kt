package com.illuminz.application.ui.gym

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.core.extensions.isNetworkActiveWithMessage
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.nearbyplaces.items.NearbyItem
import com.illuminz.application.ui.nearbyplaces.items.NearbyTitleItem
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_nearby.*

class GymFragment: DaggerBaseFragment() {

    companion object {
        const val TAG = "GymFragment"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_LOADING = 1

        fun newInstance(
        ): GymFragment {
            return GymFragment()
        }
    }
    override fun getLayoutResId(): Int = R.layout.fragment_nearby

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GymViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
        setObservers()
    }

    private fun initialise() {
        adapter = GroupAdapter()
        rvNearby.adapter = adapter

        val layoutManager = rvNearby.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = adapter.spanSizeLookup

        val toolBarTitle = getString(R.string.fragment_title_gym)

        toolbar.title = toolBarTitle

        if (requireContext().isNetworkActiveWithMessage()) {
            viewModel.getGymDetail()
        }
    }

    private fun setObservers() {
        viewModel.getGymObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    resource.data?.let { setBasicData(it) }
                }

                Status.ERROR -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                }
            }
        })
    }

    private fun setBasicData(list: List<ServiceCategoryItemDto>) {
        adapter.clear()
        val title = "You'll find something different in each of our clubs. Check out our favorites here"

        adapter.add(NearbyTitleItem(title))

        list.forEach { serviceCategoryItem ->
            adapter.add(NearbyItem(serviceCategoryItem))
        }
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}