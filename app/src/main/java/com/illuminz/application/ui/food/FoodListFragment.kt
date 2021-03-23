package com.illuminz.application.ui.food

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.extensions.*
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.core.utils.MealType
import com.illuminz.application.R
import com.illuminz.application.ui.cart.CartFragment
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.food.items.*
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.models.response.ServiceCategoryDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_menu.*
import kotlinx.android.synthetic.main.fragment_food_list.*

class FoodListFragment : DaggerBaseFragment(), FoodItem.Callback,
    VegNonVegItem.Callback, CartBarView.Callback, SearchFoodDialogFragment.Callback {
    companion object {
        const val TAG = "FoodListFragment"

        private const val VEG = 1
        private const val NON_VEG = 2
        private const val ALL = 0

        private const val KEY_ID = "KEY_ID"
        private const val KEY_TAG = "KEY_TAG"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_LOADING = 1

        fun newInstance(serviceId: String, serviceTag: String): FoodListFragment {
            val fragment = FoodListFragment()
            val arguments = Bundle()
            arguments.putString(KEY_ID, serviceId)
            arguments.putString(KEY_TAG, serviceTag)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var foodAdapter: GroupAdapter<GroupieViewHolder>
    private var menuAdapter = GroupAdapter<GroupieViewHolder>()

    private var serviceCategoryItemList = mutableListOf<ServiceCategoryItemDto>()
    private var serviceCategoryList = mutableListOf<ServiceCategoryDto>()
    private var cartList = mutableListOf<ServiceCategoryItemDto>()

    private var menuList = mutableListOf<MenuDialogItem>()

    private lateinit var serviceId: String
    private lateinit var serviceTag: String

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FoodViewModel::class.java]
    }

    override fun getLayoutResId() = R.layout.fragment_food_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
        setObservers()
    }

    private fun initialise() {
        foodAdapter = GroupAdapter()
        rvFood.adapter = foodAdapter

        cartBarView.gone()
        btnMenu.gone()

        serviceId = requireArguments().getString(KEY_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_TAG).orEmpty()

        if (requireContext().isNetworkActiveWithMessage()) {
            viewModel.getFoodProducts(serviceId, serviceTag)
        }
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        btnMenu.setOnClickListener {
            showMenuDialog(menuList)
        }

        ivSearch.setOnClickListener {
            val dialogFragment = SearchFoodDialogFragment(this)
            dialogFragment.show(childFragmentManager, "")
        }

        cartBarView.setCallback(this)
    }

    private fun setData(list: List<ServiceCategoryDto>) {
        serviceCategoryList.clear()
        serviceCategoryItemList.clear()
        menuList.clear()

        foodAdapter.clear()

        serviceCategoryList.addAll(list)

        btnMenu.visible()

        addMealTimings(vegOnly = true, nonVegOnly = false)

        // Total items of type veg or non veg
        val itemCount = getItemCount(list = list, vegStatus = VEG)

        // Add category name and items
        list.forEachIndexed{ index,serviceCategory ->
            //Category
            val title = serviceCategory.categoryName.orEmpty()
            val itemCountFormatted = if (index == 0) {
                resources.getQuantityString(
                    R.plurals.category_count,
                    itemCount,
                    itemCount
                )
            } else {
                null
            }

            val titleItem = TitleItem(title = title, items = itemCountFormatted)
            foodAdapter.add(titleItem)

            // Add items
            serviceCategory.itemsArr?.forEach { categoryItem ->
                if (categoryItem.vegStatus == VEG) {
                    val item = FoodItem(serviceCategoryItem = categoryItem, callback = this)
                    foodAdapter.add(item)
                    serviceCategoryItemList.add(categoryItem)
                }
            }
        }

        serviceCategoryList.forEach { serviceCategoryItem ->
            menuList.add(
                MenuDialogItem(
                    title = serviceCategoryItem.categoryName.orEmpty(),
                    number = serviceCategoryItem.itemsArr?.size.orZero()
                )
            )
        }
//            if (index == 0) {
//                val titleItem = TitleItem(
//                    title = list[index].categoryName.orEmpty(),
//                    items = resources.getQuantityString(
//                        R.plurals.category_count,
//                        itemCount,
//                        itemCount
//                    )
//                )
//                foodAdapter.add(titleItem)
//            } else {
//                val titleItem = TitleItem(title = list[index].categoryName.orEmpty())
//                foodAdapter.add(titleItem)
//            }
    }

    private fun setObservers() {
        viewModel.getFoodProductObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    resource.data?.let { setData(it) }
                }

                Status.ERROR -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    handleError(resource.error)
                }
            }
        })
    }

    override fun onIncreaseFoodItemClicked(foodItem: FoodItem) {
        serviceCategoryItemList.forEach {
            if (it.id == foodItem.serviceCategoryItem.id) {
                it.quantity = foodItem.serviceCategoryItem.quantity
            }
        }
//        viewModel.updateFoodList(foodItem.serviceCategoryItem)
        changeCartItems(foodItem.serviceCategoryItem)
    }

    override fun onDecreaseFoodItemClicked(foodItem: FoodItem) {
        serviceCategoryItemList.forEach {
            if (it.id == foodItem.serviceCategoryItem.id) {
                it.quantity = foodItem.serviceCategoryItem.quantity
            }
        }
//        viewModel.updateFoodList(foodItem.serviceCategoryItem)
        changeCartItems(foodItem.serviceCategoryItem)
    }

    private fun showMenuDialog(menuList: List<MenuDialogItem>) {
        menuAdapter.clear()
        val dialog = Dialog(requireContext())

        dialog.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialog_menu)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.BOTTOM or Gravity.END)
                attributes.x = context.dpToPx(24) // left margin
                attributes.y = context.dpToPx(140) // bottom margin
            }

            rvDialogMenu.adapter = menuAdapter

            menuAdapter.addAll(menuList)
            menuAdapter.setOnItemClickListener { menuItem, _ ->
                if (menuItem is MenuDialogItem) {
                    for (i in 0 until foodAdapter.itemCount) {
                        val group = foodAdapter.getGroupAtAdapterPosition(i)
                        if (group is TitleItem && group.title == menuItem.title) {
                            scrollToPosition(position = i)
                        }
                    }
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun vegOnlyClickListener(vegOnly: Boolean, nonVegOnly: Boolean) {
        foodAdapter.clear()
        addMealTimings(vegOnly, nonVegOnly)

        if (vegOnly && !nonVegOnly) {
            val itemCount = getItemCount(list = serviceCategoryList, vegStatus = VEG)
            addFoodCategoryItems(vegStatus = VEG, itemCount = itemCount)
        } else if (nonVegOnly && !vegOnly) {
            val itemCount = getItemCount(list = serviceCategoryList, vegStatus = NON_VEG)
            addFoodCategoryItems(vegStatus = NON_VEG, itemCount = itemCount)
        } else {
            val itemCount = getItemCount(list = serviceCategoryList, vegStatus = ALL)
            addFoodCategoryItems(vegStatus = ALL, itemCount = itemCount)
        }
    }

    override fun onCartBarClick() {
        val list = arrayListOf<ServiceCategoryItemDto>()
        for (i in 0 until cartList.size) {
            val item = ServiceCategoryItemDto(
                id = cartList[i].id,
                price = cartList[i].price,
                itemName = cartList[i].itemName,
                quantity = cartList[i].quantity,
                vegStatus = cartList[i].vegStatus
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

    private fun addFoodCategoryItems(vegStatus: Int? = null, itemCount: Int) {
        serviceCategoryList.forEachIndexed { index, serviceCategory ->
            val title = serviceCategoryList[index].categoryName.orEmpty()
            val itemCountFormatted = if (index == 0) {
                resources.getQuantityString(
                    R.plurals.category_count,
                    itemCount,
                    itemCount
                )
            } else {
                null
            }
            val titleItem = TitleItem(title = title, items = itemCountFormatted)
            foodAdapter.add(titleItem)

            serviceCategory.itemsArr?.forEach { categoryItem ->
                if (vegStatus == ALL || categoryItem.vegStatus == vegStatus) {
                    val item = FoodItem(serviceCategoryItem = categoryItem, callback = this)
                    foodAdapter.add(item)
                }
            }
        }

//        for (i in serviceCategoryList.indices) {
//            if (i == 0) {
//                val titleItem = TitleItem(
//                    title = serviceCategoryList[i].categoryName.orEmpty(),
//                    items = resources.getQuantityString(
//                        R.plurals.category_count,
//                        itemCount,
//                        itemCount
//                    )
//                )
//                foodAdapter.add(titleItem)
//            } else {
//                val titleItem = TitleItem(title = serviceCategoryList[i].categoryName.orEmpty())
//                foodAdapter.add(titleItem)
//            }
//
//            serviceCategoryList[i].itemsArr?.forEach { categoryItem ->
//                if (vegStatus == ALL) {
//                    val item = FoodItem(serviceCategoryItem = categoryItem, callback = this)
//                    foodAdapter.add(item)
//                } else if (categoryItem.vegStatus == vegStatus) {
//                    val item = FoodItem(serviceCategoryItem = categoryItem, callback = this)
//                    foodAdapter.add(item)
//                }
//            }
//        }
    }

    private fun addMealTimings(vegOnly: Boolean, nonVegOnly: Boolean) {
        val titleItem = TitleItem(title = "Your Buffet")

        val mealTimingItem = listOf(
            FoodTimingItem(mealType = MealType.BREAKFAST, timing = "8:30 am - 10:20 am"),
            FoodTimingItem(mealType = MealType.LUNCH, timing = "1:30 pm - 2:30 pm"),
            FoodTimingItem(mealType = MealType.DINNER, timing = "8:40 pm - 11 pm")
        )
        val mealTypeItem =
            VegNonVegItem(isVegOnly = vegOnly, isNonVegOnly = nonVegOnly, callback = this)

        foodAdapter.add(titleItem)
        foodAdapter.addAll(mealTimingItem)
        foodAdapter.add(mealTypeItem)
    }

    override fun onIncreaseSearchItemClicked(serviceCategoryItem: ServiceCategoryItemDto) {
        for (i in 0 until foodAdapter.groupCount) {
            val group = foodAdapter.getGroupAtAdapterPosition(i)
            if (group is FoodItem && group.serviceCategoryItem.id == serviceCategoryItem.id) {
                group.serviceCategoryItem.quantity = serviceCategoryItem.quantity
                foodAdapter.notifyItemChanged(i, QuantityChangedPayload)
            }
        }
        changeCartItems(serviceCategoryItem)
    }

    override fun onDecreaseSearchItemClicked(serviceCategoryItem: ServiceCategoryItemDto) {
        for (i in 0 until foodAdapter.groupCount) {
            val group = foodAdapter.getGroupAtAdapterPosition(i)
            if (group is FoodItem && group.serviceCategoryItem.id == serviceCategoryItem.id) {
                group.serviceCategoryItem.quantity = serviceCategoryItem.quantity
                foodAdapter.notifyItemChanged(i, QuantityChangedPayload)
            }
        }
        changeCartItems(serviceCategoryItem)
    }

    private fun changeCartItems(
        serviceCategoryItem: ServiceCategoryItemDto
    ) {
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
            flCartBar.visible()
            cartBarView.setItemPrice(totalPrice = totalPrice, items = itemCount)
        } else {
            cartBarView.gone()
            flCartBar.gone()
        }
    }

    private fun getItemCount(list: List<ServiceCategoryDto>, vegStatus: Int): Int {
        var count = 0
        list.forEach { serviceCategory ->
            serviceCategory.itemsArr?.forEach { serviceCategoryItem ->
                if (vegStatus == ALL || serviceCategoryItem.vegStatus == vegStatus) {
                    count += 1
                }
            }
        }
        return count
    }

    private fun scrollToPosition(position: Int) {
        rvFood.scrollToPosition(position)
    }
}