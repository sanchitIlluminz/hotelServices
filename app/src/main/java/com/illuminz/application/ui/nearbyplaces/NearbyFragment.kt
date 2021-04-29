package com.illuminz.application.ui.nearbyplaces

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.core.extensions.isNetworkActiveWithMessage
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.food.items.TitleItem
import com.illuminz.application.ui.nearbyplaces.items.NearbyItem
import com.illuminz.application.ui.nearbyplaces.items.NearbyTitleItem
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_nearby.*
import kotlinx.android.synthetic.main.fragment_nearby.toolbar

class NearbyFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "NearbyFragment"

        private const val KEY_SERVICE_ID = "KEY_SERVICE_ID"
        private const val KEY_service_TAG = "KEY_TAG"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_LOADING = 1

        fun newInstance(
            serviceId: String,
            serviceTag: String
        ): NearbyFragment {
            val fragment = NearbyFragment()
            val arguments = Bundle()
            arguments.putString(KEY_SERVICE_ID, serviceId)
            arguments.putString(KEY_service_TAG, serviceTag)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var serviceId: String
    private lateinit var serviceTag: String
    private lateinit var fragmentType: String

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NearbyViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_nearby

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

        serviceId = requireArguments().getString(KEY_SERVICE_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_service_TAG).orEmpty()

        val toolBarTitle = getString(R.string.nearby_places)

        toolbar.title = toolBarTitle

//        if (requireContext().isNetworkActiveWithMessage() && viewModel.nearbyListEmpty()) {
//            viewModel.getNearbyPlaces(serviceId, serviceTag)
//        }

        if (requireContext().isNetworkActiveWithMessage() && viewModel.nearbyListEmpty()) {
            viewModel.getNearbyPlaces()
        }
    }

    private fun setObservers() {
        viewModel.getNearbyObserver().observe(viewLifecycleOwner, Observer { resource ->
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
        val title = "Places to visit, Things to do, and more in Mumbai"

        adapter.add(NearbyTitleItem(title))

        list.forEach { serviceCategoryItem ->
            adapter.add(NearbyItem(serviceCategoryItem))
        }
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        adapter.setOnItemClickListener { item, view ->

                if (item is NearbyItem) {
                    if (parentFragmentManager.findFragmentByTag(NearbyGalleryFragment.TAG) == null) {
                        parentFragmentManager.beginTransaction()
                            .setCustomAnimations(AnimationDirection.End)
                            .replace(
                                R.id.fragmentContainer,
                                NearbyGalleryFragment.newInstance(item.serviceCategoryItem),
                                NearbyGalleryFragment.TAG
                            )
                            .addToBackStack(NearbyGalleryFragment.TAG)
                            .commit()
                    }
//                val title = item.title
//
//                ImageDialogFragment.newInstance(title,getString(R.string.ten_km_away),imageList)
//                    .show(childFragmentManager,ImageDialogFragment.TAG)

//                ConfirmDialog.newInstance(getString(R.string.order_placed),
//                    getString(R.string.order_will_be_delivered_in_time))
//                    .show(childFragmentManager, ConfirmDialog.TAG)
            }
        }
    }
}