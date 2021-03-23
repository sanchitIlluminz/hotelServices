package com.illuminz.application.ui.massage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.extensions.*
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.illuminz.application.R
import com.illuminz.application.ui.cart.CartFragment
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.food.SearchFoodDialogFragment
import com.illuminz.application.ui.massage.items.MassageItem
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_food_list.*
import kotlinx.android.synthetic.main.fragment_massage_list.*
import kotlinx.android.synthetic.main.fragment_massage_list.cartBarView
import kotlinx.android.synthetic.main.fragment_massage_list.ivSearch
import kotlinx.android.synthetic.main.fragment_massage_list.toolbar
import kotlinx.android.synthetic.main.fragment_massage_list.viewFlipper

class MassageListFragment : DaggerBaseFragment(), MassageItem.Callback,
    CartBarView.Callback, SearchMassageDialogFragment.Callback {
    companion object {
        const val TAG = "MassageListFragment"

        private const val KEY_SERVICE_ID = "KEY_SERVICE_ID"
        private const val KEY_SERVICE_TAG = "KEY_SERVICE_TAG"

        private const val FLIPPER_CHILD_RESULT =0
        private const val FLIPPER_CHILD_LOADING =1
        fun newInstance(serviceId: String, serviceTag: String): MassageListFragment{
            val fragment =  MassageListFragment()
            val arguments = Bundle()
            arguments.putString(KEY_SERVICE_ID,serviceId)
            arguments.putString(KEY_SERVICE_TAG,serviceTag)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var massageAdapter: GroupAdapter<GroupieViewHolder>

    private lateinit var serviceId:String
    private lateinit var serviceTag:String

    private var massageList = mutableListOf<ServiceCategoryItemDto>()
    private var cartList = mutableListOf<ServiceCategoryItemDto>()

    override fun getLayoutResId(): Int = R.layout.fragment_massage_list

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[MassageViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
        setObservers()
    }

    private fun initialise() {
//        toolbar.subtitle = "Timings: 10:30 AM - 8:00 PM"

//        cartBarView.setItemPrice(totalPrice = 820.00,items = 4)
        cartBarView.gone()
        cartBarView.setCallback(this)

        serviceId = requireArguments().getString(KEY_SERVICE_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_SERVICE_TAG).orEmpty()

        if (requireContext().isNetworkActiveWithMessage()){
            viewModel.getMassageList(serviceId,serviceTag)
        }

        massageAdapter = GroupAdapter()
        rvMassage.adapter = massageAdapter

    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        ivSearch.setOnClickListener {
            val dialogFragment = SearchMassageDialogFragment(this)
            dialogFragment.show(childFragmentManager, "")
        }
    }

    private fun setObservers() {
        viewModel.getMassageObserver().observe(viewLifecycleOwner, Observer { resource ->
            when(resource.status){
                Status.LOADING ->{
                    viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS ->{
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    resource.data?.let { setBasicData(it) }
                }

                Status.ERROR ->{
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    handleError(resource.error)
                }
            }
        })
    }

    private fun setBasicData(list: List<ServiceCategoryItemDto>) {
        massageAdapter.clear()
        list.forEach { serviceCategory ->
            massageAdapter.add(MassageItem(serviceCategory,this))
        }

        massageList.addAll(list)
    }



    override fun onCartBarClick() {
        val list = arrayListOf<ServiceCategoryItemDto>()
        for (i in 0 until cartList.size) {
            val item = ServiceCategoryItemDto(
                id = cartList[i].id,
                price = cartList[i].price,
                itemName = cartList[i].title,
                quantity = cartList[i].quantity
            )
            list.add(item)
        }

        if (parentFragmentManager.findFragmentByTag(CartFragment.TAG) == null) {
            val fragment = CartFragment.newInstance(TAG, list)
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .add(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onIncreaseMassageItemClicked(massageItem: MassageItem) {
        for (i in 0 until massageList.size){
            if (massageList[i].id == massageItem.serviceCategoryItem.id){
                massageList[i].quantity = massageItem.serviceCategoryItem.quantity
            }
        }
        changeCartItems(massageItem.serviceCategoryItem)
    }

    override fun onDecreaseMassageItemClicked(massageItem: MassageItem) {
        for (i in 0 until massageList.size){
            if (massageList[i].id == massageItem.serviceCategoryItem.id){
                massageList[i].quantity = massageItem.serviceCategoryItem.quantity
            }
        }
        changeCartItems(massageItem.serviceCategoryItem)
    }

    override fun onIncreaseSearchItemClicked(massageItem: MassageItem) {
        for (i in 0 until massageList.size){
            if (massageList[i].id == massageItem.serviceCategoryItem.id){
                massageList[i].quantity = massageItem.serviceCategoryItem.quantity
                massageAdapter.notifyItemChanged(i,QuantityChangedPayload)
            }
        }
        changeCartItems(massageItem.serviceCategoryItem)
    }

    override fun onDecreaseSearchItemClicked(massageItem: MassageItem) {
        for (i in 0 until massageList.size){
            if (massageList[i].id == massageItem.serviceCategoryItem.id){
                massageList[i].quantity = massageItem.serviceCategoryItem.quantity
                massageAdapter.notifyItemChanged(i,QuantityChangedPayload)
            }
        }
        changeCartItems(massageItem.serviceCategoryItem)
    }

    fun changeCartItems(serviceCategoryItem: ServiceCategoryItemDto){
        var itemCount = 0
        var totalPrice = 0.0

        //Check if item is already present in list or not
        var newItem = cartList.find {
            (it.id == serviceCategoryItem.id)
        }

        when (newItem) {
            null -> {
                // Add item to list
                cartList.add(serviceCategoryItem)
            }
            else -> {
                // Changes to item in list
                for (i in 0 until cartList.size) {
                    if (cartList[i].id == serviceCategoryItem.id) {
                        //Remove item if quantity is zero else update quantity
                        if (serviceCategoryItem.quantity == 0) {
                            cartList.removeAt(i)
                            break
                        } else {
                            cartList[i].quantity = serviceCategoryItem.quantity
                        }
                    }
                }
            }
        }

        // Calculate total price and item count
        cartList.forEach {
            itemCount += it.quantity
            totalPrice += (it.price.orZero() * it.quantity)
        }

        // Set visibilty and data of cartBar
        if (cartList.size != 0) {
            cartBarView.visible()
            cartBarView.setItemPrice(totalPrice = totalPrice, items = itemCount)
        } else {
            cartBarView.gone()
        }
    }
}