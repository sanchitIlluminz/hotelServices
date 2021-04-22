package com.illuminz.application.ui.laundry

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.extensions.isNetworkActiveWithMessage
import com.core.extensions.orZero
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.laundry.items.LaundryItem
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_laundry_list.*
import kotlinx.android.synthetic.main.fragment_laundry_list.viewFlipper

class LaundryListFragment : DaggerBaseFragment(), LaundryItem.Callback {
    companion object {
        const val TAG = "LaundryListFragment"

        private const val KEY_SERVICE_ID = "KEY_SERVICE_ID"
        private const val KEY_SERVICE_TAG = "KEY_SERVICE_TAG"
        private const val KEY_LAUNDRY_TYPE = "KEY_LAUNDRY_TYPE"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_LOADING = 1

        fun newInstance(
            laundryType: Int,
            serviceId: String,
            serviceTag: String
        ): LaundryListFragment {
            val fragment = LaundryListFragment()
            val arguments = Bundle()
            arguments.putString(KEY_SERVICE_ID, serviceId)
            arguments.putString(KEY_SERVICE_TAG, serviceTag)
            arguments.putInt(KEY_LAUNDRY_TYPE, laundryType)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var laundryAdapter: GroupAdapter<GroupieViewHolder>

    private lateinit var serviceId: String
    private lateinit var serviceTag: String
    private var laundryType = -1

    private var onlyIronCartList = mutableListOf<ServiceCategoryItemDto>()
    private var washIronCartList = mutableListOf<ServiceCategoryItemDto>()

    private var onlyIronList = mutableListOf<ServiceCategoryItemDto>()
    private var washIronList = mutableListOf<ServiceCategoryItemDto>()
    private var laundryItemList = mutableListOf<ServiceCategoryItemDto>()

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment(), viewModelFactory)[LaundryViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_laundry_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setBasicData()
        setObservers()
    }

    private fun initialise() {
        laundryAdapter = GroupAdapter()
        rvLaundry.adapter = laundryAdapter

        laundryType = requireArguments().getInt(KEY_LAUNDRY_TYPE).orZero()
        serviceId = requireArguments().getString(KEY_SERVICE_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_SERVICE_TAG).orEmpty()



//        if (requireContext().isNetworkActiveWithMessage()) {
//            viewModel.getLaundryDetails(serviceId, serviceTag)
//        }
    }

    private fun setObservers() {
        viewModel.getLaundryObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
//                    initialiseCartList()
                   setBasicData()
                }

                Status.ERROR -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    handleError(resource.error)
                }
            }
        })
    }

    private fun setBasicData() {

        when (laundryType) {
            AppConstants.LAUNDRY_ONLY_IRON -> {
                laundryAdapter.clear()
                onlyIronList.clear()
                onlyIronList.addAll(viewModel.getOnlyIronList())
                onlyIronList.forEach { serviceCategoryItem ->
                    laundryAdapter.add(
                        LaundryItem(
                            serviceCategoryItem = serviceCategoryItem,
                            laundryType = AppConstants.LAUNDRY_ONLY_IRON,
                            callback = this
                        )
                    )
                    laundryItemList.add(serviceCategoryItem)
                }

            }

            AppConstants.LAUNDRY_WASH_IRON -> {
                laundryAdapter.clear()
                washIronList.clear()
                washIronList.addAll(viewModel.getWashIronList())
                washIronList.forEach { serviceCategoryItem ->
                    laundryAdapter.add(
                        LaundryItem(
                            serviceCategoryItem = serviceCategoryItem,
                            laundryType = AppConstants.LAUNDRY_WASH_IRON,
                            callback = this
                        )
                    )
                    laundryItemList.add(serviceCategoryItem)
                }
            }
            else -> {
                val a=10
            }
        }
    }

    override fun onIncreaseLaundryItemClicked(laundryItem: LaundryItem) {
        viewModel.updateCartList(laundryItem.serviceCategoryItem)
    }

    override fun onDecreaseLaundryItemClicked(laundryItem: LaundryItem) {
        viewModel.updateCartList(laundryItem.serviceCategoryItem)
    }

    fun getLaundryType(): Int = laundryType

    fun updateLaundryAdapter(laundryItem: LaundryItem) {
        val itemCount = laundryAdapter.itemCount
//
        for (i in 0 until itemCount) {
            val group = laundryAdapter.getGroupAtAdapterPosition(i)
            if (group is LaundryItem && group.serviceCategoryItem.id == laundryItem.serviceCategoryItem.id) {
                group.serviceCategoryItem.quantity = laundryItem.serviceCategoryItem.quantity
                laundryAdapter.notifyItemChanged(i, QuantityChangedPayload)
            }
        }
    }
}