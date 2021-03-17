package com.illuminz.application.ui.laundry

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.extensions.isNetworkActiveWithMessage
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.food.items.LaundryItem
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryDto
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
            laundryType: String,
            serviceId: String,
            serviceTag: String
        ): LaundryListFragment {
            val fragment = LaundryListFragment()
            val arguments = Bundle()
            arguments.putString(KEY_SERVICE_ID, serviceId)
            arguments.putString(KEY_SERVICE_TAG, serviceTag)
            arguments.putString(KEY_LAUNDRY_TYPE, laundryType)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var laundryType: String

    private lateinit var laundryAdapter: GroupAdapter<GroupieViewHolder>

    private lateinit var serviceId: String
    private lateinit var serviceTag: String

    private var onlyIronCartList = mutableListOf<ServiceCategoryDto>()
    private var washIronCartList = mutableListOf<ServiceCategoryDto>()
    private var laundryItemList = mutableListOf<ServiceCategoryDto>()
    private var cartList = mutableListOf<ServiceCategoryDto>()

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment(), viewModelFactory)[LaundryViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_laundry_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        setListeners()
        setObservers()
    }

    private fun initialise() {
        laundryAdapter = GroupAdapter()
        rvLaundry.adapter = laundryAdapter

        laundryType = requireArguments().getString(KEY_LAUNDRY_TYPE).orEmpty()
        serviceId = requireArguments().getString(KEY_SERVICE_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_SERVICE_TAG).orEmpty()
//        val parentFrag = requireParentFragment()
//        if (parentFrag is LaundryFragment){
//            serviceId = parentFrag.getServiceId()
//            serviceTag = parentFrag.getServiceTag()
//        }
        if (requireContext().isNetworkActiveWithMessage()) {
            viewModel.getLaundryDetails(serviceId, serviceTag,laundryType)
        }
//        val item = listOf(
//            LaundryItem(title = "Shirt",price = 100.00,quantity = 1,callback = this,foodItem = false),
//            FoodListItem(title = "T-Shirt",price = 150.00,callback = this,foodItem = false),
//            FoodListItem(title = "Jeans",price = 250.00,quantity = 1,callback = this,foodItem = false),
//            FoodListItem(title = "Trousers",price = 350.00,callback = this,foodItem = false),
//            FoodListItem(title = "kurta",price = 150.00,quantity = 1,callback = this,foodItem = false),
//            FoodListItem(title = "Blazer",price = 250.00,callback = this,foodItem = false),
//            FoodListItem(title = "Saree",price = 150.00,quantity = 1,callback = this,foodItem = false),
//            FoodListItem(title = "Pyjama",price = 950.00,callback = this,foodItem = false),
//            FoodListItem(title = "Jacket",price = 150.00,quantity = 1,callback = this,foodItem = false),
//            FoodListItem(title = "Shirt",price = 100.00,quantity = 1,callback = this,foodItem = false),
//            FoodListItem(title = "T-Shirt",price = 150.00,callback = this,foodItem = false),
//            FoodListItem(title = "Jeans",price = 250.00,quantity = 1,callback = this,foodItem = false),
//            FoodListItem(title = "Trousers",price = 350.00,callback = this,foodItem = false)
//        )
//
//        listAdapter.addAll(item)
    }

    private fun setListeners() {

    }

    private fun setObservers() {
        viewModel.getLaundaryObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    resource.data?.let { setBasicData() }
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
            AppConstants.LAUNDARY_ONLY_IRON -> {
                laundryAdapter.clear()
               val list = viewModel.getOnlyIronList()
                list.forEach { serviceCategory ->
                    laundryAdapter.add(
                        LaundryItem(
                            serviceCategoryDto = serviceCategory,
                            laundryType = AppConstants.LAUNDARY_ONLY_IRON,
                            callback = this
                        )
                    )
                    laundryItemList.add(serviceCategory)
                }


            }

            AppConstants.LAUNDARY_WASH_IRON -> {
                laundryAdapter.clear()
                val list = viewModel.getwashIronList()
                list.forEach { serviceCategory ->
                    laundryAdapter.add(
                        LaundryItem(
                            serviceCategoryDto = serviceCategory,
                            laundryType = AppConstants.LAUNDARY_WASH_IRON,
                            callback = this
                        )
                    )
                    laundryItemList.add(serviceCategory)
                }
            }
            else -> {
            }
        }
    }


    override fun onIncreaseLaundryItemClicked(laundryItem: LaundryItem) {
        laundryItemList.forEach { serviceCategory ->
            if (serviceCategory.id == laundryItem.serviceCategoryDto.id) {
                serviceCategory.quantity = laundryItem.serviceCategoryDto.quantity
            }
        }
        if (laundryItem.laundryType == AppConstants.LAUNDARY_ONLY_IRON){
            changeCartList(laundryItem.serviceCategoryDto,onlyIronCartList)
            viewModel.updateCartItems(onlyIronCartList,AppConstants.LAUNDARY_ONLY_IRON)
        }else{
            changeCartList(laundryItem.serviceCategoryDto,washIronCartList)
            viewModel.updateCartItems(washIronCartList,AppConstants.LAUNDARY_WASH_IRON)
        }
    }


    override fun onDecreaseLaundryItemClicked(laundryItem: LaundryItem) {
        laundryItemList.forEach { serviceCategory ->
            if (serviceCategory.id == laundryItem.serviceCategoryDto.id) {
                serviceCategory.quantity = laundryItem.serviceCategoryDto.quantity
            }
        }
        if (laundryItem.laundryType == AppConstants.LAUNDARY_ONLY_IRON){
            changeCartList(laundryItem.serviceCategoryDto,onlyIronCartList)
            viewModel.updateCartItems(onlyIronCartList,AppConstants.LAUNDARY_ONLY_IRON)
        }else{
            changeCartList(laundryItem.serviceCategoryDto,washIronCartList)
            viewModel.updateCartItems(washIronCartList,AppConstants.LAUNDARY_WASH_IRON)
        }
    }

    private fun changeCartList(serviceCategoryDto: ServiceCategoryDto, cartList:MutableList<ServiceCategoryDto>) {
        //Check if item is already present in list or not
        var newItem = cartList.find {
            (it.id == serviceCategoryDto.id)
        }

        when (newItem) {
            null -> {
                // Add item to list
                cartList.add(serviceCategoryDto)
            }
            else -> {
                // Changes to item in list
                for (i in 0 until cartList.size) {
                    if (cartList[i].id == serviceCategoryDto.id) {
                        //Remove item if quantity is zero else update quantity
                        if (serviceCategoryDto.quantity == 0) {
                            cartList.removeAt(i)
                            break
                        } else {
                            cartList[i].quantity = serviceCategoryDto.quantity
                        }
                    }
                }
            }
        }
    }
}