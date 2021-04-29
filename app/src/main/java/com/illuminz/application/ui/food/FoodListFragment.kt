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
import com.illuminz.application.ui.cart.FoodCartFragment
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.custom.ErrorView
import com.illuminz.application.ui.food.items.*
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.request.CartItemDetail
import com.illuminz.data.models.response.BuffetDto
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.models.response.ServiceCategoryDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_menu.*
import kotlinx.android.synthetic.main.fragment_food_list.*

class FoodListFragment(

) : DaggerBaseFragment(), FoodItem.Callback,
    VegNonVegItem.Callback, CartBarView.Callback, SearchFoodDialogFragment.Callback,
    ErrorView.ErrorButtonClickListener {
    companion object {
        const val TAG = "FoodListFragment"

        private const val VEG = 1
        private const val NON_VEG = 0
        private const val ALL = 2

        private const val KEY_SERVICE_ID = "KEY_ID"
        private const val KEY_SERVICE_TAG = "KEY_TAG"
        private const val KEY_BUFFET = "KEY_BUFFET"
        private const val KEY_OUTLET_ID = "KEY_OUTLET_ID"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_CONNECTION_ERROR = 2
        private const val FLIPPER_CHILD_LOADING = 1

        fun newInstance(
            serviceId: String,
            serviceTag: String,
            buffet: ArrayList<BuffetDto>,
            outletId: String
        ): FoodListFragment {
            val fragment = FoodListFragment()
            val arguments = Bundle()
            arguments.putString(KEY_SERVICE_ID, serviceId)
            arguments.putString(KEY_SERVICE_TAG, serviceTag)
            arguments.putString(KEY_OUTLET_ID, outletId)
            arguments.putParcelableArrayList(KEY_BUFFET,buffet)
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

    private var buffetList = mutableListOf<BuffetDto>()

    private var cartBarViewVisible = false

    private lateinit var serviceId: String
    private lateinit var serviceTag: String
    private lateinit var outletId: String

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

        serviceId = requireArguments().getString(KEY_SERVICE_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_SERVICE_TAG).orEmpty()
        outletId = requireArguments().getString(KEY_OUTLET_ID).orEmpty()
        buffetList.clear()
        buffetList.addAll(requireArguments().getParcelableArrayList<BuffetDto>(KEY_BUFFET).orEmpty())

        if (requireContext().isNetworkActiveWithMessage()) {
            viewModel.getFoodProducts(serviceId, serviceTag)
        }
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        btnMenu.setOnClickListener {
            showMenuDialog(menuList)
        }

        ivSearch.setOnClickListener {
            val dialogFragment = SearchFoodDialogFragment(this)
            dialogFragment.show(childFragmentManager, "")
        }

        cartBarView.setCallback(this)

        connectionErrorView.setErrorButtonClickListener(this)
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
                    viewFlipper.displayedChild = FLIPPER_CHILD_CONNECTION_ERROR
                    handleError(resource.error)
                }
            }
        })
    }

    private fun setData(list: List<ServiceCategoryDto>) {
        serviceCategoryList.clear()
        serviceCategoryItemList.clear()
        menuList.clear()
        foodAdapter.clear()


//        serviceCategoryList.addAll(list)
        list.forEach { item ->
            if (item.outletId == outletId){
                serviceCategoryList.add(item)
            }
        }
        serviceCategoryList.forEach { serviceCategoryItem ->
            if (serviceCategoryItem.outletId == outletId){
                serviceCategoryItem.itemsArr?.forEach {
                    serviceCategoryItemList.add(it)
                }
            }
        }

        viewModel.updateFoodList(serviceCategoryItemList)
        val savedCartList = viewModel.getSavedCartList()
        //Update cartList when returning from cart fragment
        if (cartList.size != 0 || savedCartList?.size!=0 ) {
            cartList.clear()

            savedCartList?.forEach { savedCartItem ->
                serviceCategoryItemList.forEach { serviceCategoryItem ->
                    if (serviceCategoryItem.id == savedCartItem.id) {
                        cartList.add(serviceCategoryItem)
                    }
                }
            }
        }
        btnMenu.visible()

        addMealTimings(vegOnly = false,nonVegOnly = false)

        // Total items of type veg
        val itemCount = getInitialItemCount(list = serviceCategoryList, vegStatus = ALL)

        // Add category name and items
        serviceCategoryList.forEachIndexed { index, serviceCategory ->
            if (serviceCategory.outletId == outletId){
                //Category
                val title = serviceCategory.categoryName.orEmpty()

                //With first title item count is also added
                val itemCountFormatted = if (index == 0) {
                    resources.getQuantityString(
                        R.plurals.category_count,
                        itemCount,
                        itemCount
                    )
                } else {
                    null
                }

                val foodCategoryCount = serviceCategory.itemsArr?.size.orZero()
                val titleItem = TitleItem(
                    title = title,
                    totalItemCount = itemCountFormatted,
                    foodCategoryCount = foodCategoryCount
                )
                foodAdapter.add(titleItem)

                // Add items
                serviceCategory.itemsArr?.forEach { categoryItem ->
//                if (categoryItem.vegStatus == VEG) {
                    val item = FoodItem(serviceCategoryItem = categoryItem, callback = this)
                    foodAdapter.add(item)
//                    serviceCategoryItemList.add(categoryItem)
//                }
                }
            }
        }

        serviceCategoryList.forEach { serviceCategoryItem ->
            val categoryItemCount = serviceCategoryItem.itemsArr?.size.orZero()
            if (categoryItemCount!=0){
                menuList.add(
                    MenuDialogItem(
                        title = serviceCategoryItem.categoryName.orEmpty(),
                        number = categoryItemCount
                    )
                )
            }
        }

        if (cartList.size != 0) {
            setCartItems()
        }
    }

    override fun onIncreaseFoodItemClicked(foodItem: FoodItem) {
        serviceCategoryItemList.forEach { serviceCategoryItem ->
            if (serviceCategoryItem.id == foodItem.serviceCategoryItem.id) {
                serviceCategoryItem.quantity = foodItem.serviceCategoryItem.quantity
            }
        }
        changeCartItems(foodItem.serviceCategoryItem)
    }

    override fun onDecreaseFoodItemClicked(foodItem: FoodItem) {
        serviceCategoryItemList.forEach { serviceCategoryItem ->
            if (serviceCategoryItem.id == foodItem.serviceCategoryItem.id) {
                serviceCategoryItem.quantity = foodItem.serviceCategoryItem.quantity
            }
        }
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
                val marginY = if (cartBarViewVisible)
                    140
                else
                    80
                setGravity(Gravity.BOTTOM or Gravity.END)
                attributes.x = context.dpToPx(24) // left margin
                attributes.y = context.dpToPx(marginY) // bottom margin
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
            val itemCount = getItemCount(list = serviceCategoryItemList, vegStatus = VEG)
            addFoodCategoryItems(vegStatus = VEG, totalItemCount = itemCount)
            setMenuList(VEG)
        } else if (nonVegOnly && !vegOnly) {
            val itemCount = getItemCount(list = serviceCategoryItemList, vegStatus = NON_VEG)
            addFoodCategoryItems(vegStatus = NON_VEG, totalItemCount = itemCount)
            setMenuList(NON_VEG)
        } else {
            val itemCount = getItemCount(list = serviceCategoryItemList, vegStatus = ALL)
            addFoodCategoryItems(vegStatus = ALL, totalItemCount = itemCount)
            setMenuList()
        }
    }

    private fun addFoodCategoryItems(vegStatus: Int? = null, totalItemCount: Int) {
        serviceCategoryList.forEachIndexed { index, serviceCategory ->
            val title = serviceCategoryList[index].categoryName.orEmpty()
            val itemCountFormatted = if (index == 0) {
                resources.getQuantityString(
                    R.plurals.category_count,
                    totalItemCount,
                    totalItemCount
                )
            } else {
                null
            }

            val categoryItemCount = serviceCategory.itemsArr?.filter { serviceCategoryItem ->
                (serviceCategoryItem.vegStatus == vegStatus) ||
                        (vegStatus == ALL)
            }?.size.orZero()

            val titleItem = TitleItem(
                title = title,
                totalItemCount = itemCountFormatted,
                foodCategoryCount = categoryItemCount
            )
            foodAdapter.add(titleItem)

            serviceCategory.itemsArr?.forEach { categoryItem ->
                if (vegStatus == ALL || categoryItem.vegStatus == vegStatus) {
                    val item = FoodItem(serviceCategoryItem = categoryItem, callback = this)
                    foodAdapter.add(item)
                }
            }
        }
    }

    private fun addMealTimings(vegOnly: Boolean, nonVegOnly: Boolean) {
        val titleItem = TitleItem(title = "Your Buffet")
        foodAdapter.add(titleItem)

        buffetList.forEach { buffet ->
           val item =  when(buffet.title){
                "Breakfast" -> { FoodTimingItem(mealType = MealType.BREAKFAST, timing = getBuffetTiming(buffet))}
                "Lunch" -> { FoodTimingItem(mealType = MealType.LUNCH, timing = getBuffetTiming(buffet))}
                else -> { FoodTimingItem(mealType = MealType.DINNER, timing = getBuffetTiming(buffet))}
            }
            foodAdapter.add(item)
        }

//        val mealTimingItem = listOf(
//            FoodTimingItem(mealType = MealType.BREAKFAST, timing = "8:30 am - 10:20 am"),
//            FoodTimingItem(mealType = MealType.LUNCH, timing = "1:30 pm - 2:30 pm"),
//            FoodTimingItem(mealType = MealType.DINNER, timing = "8:40 pm - 11 pm")
//        )
        val mealTypeItem =
            VegNonVegItem(isVegOnly = vegOnly, isNonVegOnly = nonVegOnly, callback = this)

//        foodAdapter.addAll(mealTimingItem)
        foodAdapter.add(mealTypeItem)
    }

    private fun getBuffetTiming(buffetDto: BuffetDto): String {
        var startHour = buffetDto.startTime?.div(60)
        var endHour = buffetDto.endTime?.div(60)

        val startMinutes = buffetDto.startTime?.orZero()?.rem(60)
        val endMinutes = buffetDto.endTime?.orZero()?.rem(60)

        val startMin = String.format("%02d",startMinutes)
        val endMin = String.format("%02d",endMinutes)

        val startTimePeriod = if (startHour.orZero() <12){
            "am"
        }else{
            startHour = startHour?.minus(12)
            "pm"
        }

        val endTimePeriod = if (endHour.orZero() <12){
            "am"
        }else{
            if (endHour.orZero()!=12){
                endHour = endHour?.minus(12)
            }
            "pm"
        }

        return "${startHour}:$startMin $startTimePeriod - ${endHour}:$endMin $endTimePeriod"
    }

    override fun onCartBarClick() {
        val list = arrayListOf<CartItemDetail>()
        for (i in 0 until cartList.size) {
            val item = CartItemDetail(
                id = cartList[i].id,
                quantity = cartList[i].quantity
            )
            list.add(item)
        }

        viewModel.addSavedCart(list)

        if (parentFragmentManager.findFragmentByTag(FoodCartFragment.TAG) == null) {
            val fragment = FoodCartFragment.newInstance(TAG, list)
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
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
        cartList.forEach { serviceCategoryItem ->
            itemCount += serviceCategoryItem.quantity
            totalPrice += (serviceCategoryItem.price.orZero() * serviceCategoryItem.quantity)
        }

        // Set visibilty and data of cartBar
        if (cartList.size != 0) {
            cartBarView.visible()
            cartBarViewVisible = true
            flCartBar.visible()
            cartBarView.setItemPrice(totalPrice = totalPrice, items = itemCount)
        } else {
            cartBarView.gone()
            cartBarViewVisible = false
            flCartBar.gone()
        }
    }

    private fun setCartItems() {
        var itemCount = 0
        var totalPrice = 0.0

        // Calculate total price and item count
        cartList.forEach { serviceCategoryItem ->
            itemCount += serviceCategoryItem.quantity
            totalPrice += (serviceCategoryItem.price.orZero() * serviceCategoryItem.quantity)
        }

        cartBarView.visible()
        cartBarViewVisible = true
        flCartBar.visible()
        cartBarView.setItemPrice(totalPrice = totalPrice, items = itemCount)
    }

    private fun getItemCount(list: List<ServiceCategoryItemDto>, vegStatus: Int): Int {
        var count = 0
        list.forEach { serviceCategoryItem ->
            if (vegStatus == ALL || serviceCategoryItem.vegStatus == vegStatus) {
                count += 1
            }
        }
        return count
    }

    private fun scrollToPosition(position: Int) {
        rvFood.scrollToPosition(position)
    }

    private fun getInitialItemCount(list: List<ServiceCategoryDto>, vegStatus: Int): Int {
        var count = 0
        list.forEach { serviceCategory ->
            if (serviceCategory.outletId == outletId){
                serviceCategory.itemsArr?.forEach { serviceCategoryItem ->
                    if (vegStatus == ALL
//                    || serviceCategoryItem.vegStatus == vegStatus
                    ) {
                        count += 1
                    }
                }
            }
        }
        return count
    }

    private fun setMenuList(type: Int? = null) {
        menuList.clear()

        serviceCategoryList.forEach { serviceCategory ->
            val title = serviceCategory.categoryName.orEmpty()
            var number = if (type == VEG || type == NON_VEG) {
                var count = 0
                serviceCategory.itemsArr?.forEach { serviceCategoryItem ->
                    if (serviceCategoryItem.vegStatus == type) {
                        count += 1
                    }
                }
                count
            } else {
                serviceCategory.itemsArr?.size.orZero()
            }

            if (number != 0) {
                menuList.add(
                    MenuDialogItem(
                        title = title,
                        number = number
                    )
                )
            }
        }

        if (menuList.size == 0) {
            menuList.add(
                MenuDialogItem(
                    title = "No Items Available", number = 0
                )
            )
        }
    }

    override fun onErrorButtonClicked() {
        if (requireContext().isNetworkActiveWithMessage()) {
            viewModel.getFoodProducts(serviceId, serviceTag)
        }
    }
}