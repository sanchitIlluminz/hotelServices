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
    private var laundryItemList = mutableListOf<ServiceCategoryItemDto>()

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment(), viewModelFactory)[LaundryViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_laundry_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setObservers()
    }

    private fun initialise() {
        laundryAdapter = GroupAdapter()
        rvLaundry.adapter = laundryAdapter

        laundryType = requireArguments().getInt(KEY_LAUNDRY_TYPE).orZero()
        serviceId = requireArguments().getString(KEY_SERVICE_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_SERVICE_TAG).orEmpty()



        if (requireContext().isNetworkActiveWithMessage()) {
            viewModel.getLaundryDetails(serviceId, serviceTag, laundryType)
        }
    }

    private fun setObservers() {
        viewModel.getLaundaryObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    initialiseCartList()
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

                val laundryList = viewModel.getOnlyIronList()
                laundryList.forEach { serviceCategoryItem ->
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

                val laundryList = viewModel.getwashIronList()
                laundryList.forEach { serviceCategoryItem ->
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
        //Update laundrylist in class
        laundryItemList.forEach { serviceCategoryItem ->
            if ((serviceCategoryItem.id == laundryItem.serviceCategoryItem.id) &&
                (serviceCategoryItem.laundryType == laundryItem.serviceCategoryItem.laundryType)
            ) {
                serviceCategoryItem.quantity = laundryItem.serviceCategoryItem.quantity
            }
        }

        //Update laundrylist in viewmodel
        viewModel.updateOriginalLaundryList(laundryItem)

        //Update cartList both locally and in viewmodel
        var laundryType = -1

        val cartList = if (laundryItem.laundryType == AppConstants.LAUNDRY_ONLY_IRON) {
            laundryType = AppConstants.LAUNDRY_ONLY_IRON
            onlyIronCartList
        } else {
            laundryType = AppConstants.LAUNDRY_WASH_IRON
            washIronCartList
        }

        changeLocalCartList(
            serviceCategoryItemDto = laundryItem.serviceCategoryItem,
            cartList = cartList
        )
        viewModel.updateCartList(
            cartList = cartList,
            laundryType = laundryType
        )
    }

    override fun onDecreaseLaundryItemClicked(laundryItem: LaundryItem) {
        //Update laundrylist in class
        laundryItemList.forEach { serviceCategoryItem ->
            if ((serviceCategoryItem.id == laundryItem.serviceCategoryItem.id) &&
                (serviceCategoryItem.laundryType == laundryItem.serviceCategoryItem.laundryType)
            ) {
                serviceCategoryItem.quantity = laundryItem.serviceCategoryItem.quantity
            }
        }

        //Update laundrylist in viewmodel
        viewModel.updateOriginalLaundryList(laundryItem)

        //Update cartList both locally and in viewmodel
        var laundryType = -1

        val cartList = if (laundryItem.laundryType == AppConstants.LAUNDRY_ONLY_IRON) {
            laundryType = AppConstants.LAUNDRY_ONLY_IRON
            onlyIronCartList
        } else {
            laundryType = AppConstants.LAUNDRY_WASH_IRON
            washIronCartList
        }

        changeLocalCartList(
            serviceCategoryItemDto = laundryItem.serviceCategoryItem,
            cartList = cartList
        )

        viewModel.updateCartList(
            cartList = cartList,
            laundryType = laundryType
        )
    }

    /**
    cartlist for individual fragment is maintained here while
    Final cartList is maintained in the viewmodel

    params
    serviceCategoryDto - item whose quantity is changed
    cartList - individual fragment cart list
     */
    private fun changeLocalCartList(
        serviceCategoryItemDto: ServiceCategoryItemDto,
        cartList: MutableList<ServiceCategoryItemDto>
    ) {
        //Check if item is already present in list or not
        val newItem = cartList.find {   serviceCategoryItem ->
            (serviceCategoryItem.id == serviceCategoryItemDto.id)
        }

        when (newItem) {
            null -> {
                // Add item to list
                cartList.add(serviceCategoryItemDto)
            }
            else -> {
                // Changes to item in list
                for (i in 0 until cartList.size) {
                    if (cartList[i].id == serviceCategoryItemDto.id) {
                        //Remove item if quantity is zero else update quantity
                        if (serviceCategoryItemDto.quantity == 0) {
                            cartList.removeAt(i)
                            break
                        } else {
                            cartList[i].quantity = serviceCategoryItemDto.quantity
                        }
                    }
                }
            }
        }
    }

    fun getLaundryType(): Int = laundryType

    fun updateLaundryAdapter(laundryItem: LaundryItem) {
        val itemCount = laundryAdapter.itemCount

        for (i in 0 until itemCount) {
            val group = laundryAdapter.getGroupAtAdapterPosition(i)
            if (group is LaundryItem && group.serviceCategoryItem.id == laundryItem.serviceCategoryItem.id) {
                group.serviceCategoryItem.quantity = laundryItem.serviceCategoryItem.quantity
                laundryAdapter.notifyItemChanged(i, QuantityChangedPayload)
            }
        }

        //Update CartList locally and in viewmodel
        var laundryType:Int = -1

        val cartList = if (laundryItem.laundryType == AppConstants.LAUNDRY_ONLY_IRON) {
            laundryType = AppConstants.LAUNDRY_ONLY_IRON
            onlyIronCartList
        } else {
            laundryType = AppConstants.LAUNDRY_WASH_IRON
            washIronCartList
        }

        changeLocalCartList(
            serviceCategoryItemDto = laundryItem.serviceCategoryItem,
            cartList = cartList
        )
        viewModel.updateCartList(
            cartList = cartList,
            laundryType = laundryType
        )
    }

    private fun initialiseCartList(){
       if (!viewModel.savedCartEmptyOrNull()){
            if ((laundryType == AppConstants.LAUNDRY_ONLY_IRON) && (onlyIronCartList.size!=0)){
                onlyIronCartList.addAll(viewModel.getOnlyIronCartList())
            }else if ((laundryType == AppConstants.LAUNDRY_WASH_IRON) && (washIronCartList.size!=0)){
                washIronCartList.addAll(viewModel.getWashIronCartList())
            }
       }
    }


}