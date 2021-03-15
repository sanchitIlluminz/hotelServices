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
import com.core.extensions.dpToPx
import com.core.extensions.isNetworkActiveWithMessage
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.illuminz.application.R
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.food.items.*
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.FoodDto
import com.illuminz.data.models.response.ServiceProductDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_menu.*
import kotlinx.android.synthetic.main.fragment_food_list.*


class FoodListFragment : DaggerBaseFragment(), FoodItem.Callback,
    VegNonVegItem.Callback, CartBarView.Callback, SearchDialogFragment.Callback {
    companion object {
        const val TAG = "FoodListFragment"
        private const val VEG = 1
        private const val NONVEG = 2
        private const val ALL = 0
        private const val KEY_ID = "KEY_ID"
        private const val KEY_TAG = "KEY_TAG"

        fun newInstance(id: String? = null, tag: String? = null): FoodListFragment {
            val fragment = FoodListFragment()
            val args = Bundle()
            args.putString(KEY_ID, id)
            args.putString(KEY_TAG, tag)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var foodAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var menuList: List<MenuDialogItem>
    private var foodList = mutableListOf<FoodDto>()
    private var snackList = mutableListOf<FoodDto>()
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

    private fun setObservers() {
        viewModel.getFoodProductObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    resource.data?.let { setData(it) }
                }
                Status.ERROR -> {
                    dismissLoading()
                    handleError(resource.error)
                }
            }
        })
    }

    private fun setData(list: List<ServiceProductDto>) {
        foodAdapter.clear()
        foodList.clear()

        addMealDetails(vegOnly = true, nonVegOnly = false)
        val item1 = TitleItem(title = "Todays Special", items = "230 ITEMS")

        foodAdapter.add(item1)

        list.forEach {
            it.itemsArr?.forEach { foodDto ->
                foodList.add(foodDto)
                if (foodDto.vegStatus == 1) {
                    val item = FoodItem(foodDto = foodDto, callback = this)
                    foodAdapter.add(item)
                }
            }
        }

        val item2 = TitleItem(title = "Snacks")
        foodAdapter.add(item2)

        list.forEach {
            it.itemsArr?.forEach { foodDto ->
                snackList.add(foodDto)
                if (foodDto.vegStatus == 1){
                    val item = FoodItem(foodDto = foodDto, callback = this,hideThumbnail = true)
                    foodAdapter.add(item)
                }
            }
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
            val dialogFragment = SearchDialogFragment(this)
            dialogFragment.show(childFragmentManager, "")
        }

        cartBarView.setCallback(this)
    }

    private fun initialise() {
//        cartBarView.setButtonText(getString(R.string.view_cart))
//        cartBarView.setItemPrice(totalPrice = 820.00, items = 4)

        foodAdapter = GroupAdapter()
        rvFood.adapter = foodAdapter

        serviceId = arguments?.getString(KEY_ID).orEmpty()
        serviceTag = arguments?.getString(KEY_TAG).orEmpty()

        if (context?.isNetworkActiveWithMessage() == true) {
            viewModel.getFoodProducts(serviceId, serviceTag)
        }

        menuList = listOf(
            MenuDialogItem(title = "Today’s special", number = 6),
            MenuDialogItem(title = "Snacks", number = 8),
            MenuDialogItem(title = "Breakfast", number = 12),
            MenuDialogItem(title = "Starters", number = 20),
            MenuDialogItem(title = "Main Course", number = 5),
            MenuDialogItem(title = "Beverages", number = 11),
            MenuDialogItem(title = "Combos", number = 9)
        )
    }

    override fun onIncreaseFoodItemClicked(foodItem: FoodItem) {
        foodList.forEach {
            if (it.id == foodItem.foodDto.id) {
                it.quantity = foodItem.foodDto.quantity
            }
        }
        viewModel.updateFoodList(foodItem.foodDto)
    }

    override fun onDecreaseFoodItemClicked(foodItem: FoodItem) {
        foodList.forEach {
            if (it.id == foodItem.foodDto.id) {
                it.quantity = foodItem.foodDto.quantity
            }
        }
        viewModel.updateFoodList(foodItem.foodDto)
    }

    private fun showMenuDialog(menuList: List<MenuDialogItem>) {
        val dialog = context?.let { Dialog(it) }

        var menuAdapter: GroupAdapter<GroupieViewHolder> = GroupAdapter()
        dialog?.run {

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialog_menu)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.BOTTOM or Gravity.END)
//                val layoutParams = attributes


                attributes.x = context.dpToPx(24) // left margin
                attributes.y = context.dpToPx(140) // bottom margin
//
//                window?.attributes = layoutParams
            }

            rvDialogMenu.adapter = menuAdapter
//
            menuAdapter.addAll(menuList)
            menuAdapter.setOnItemClickListener { item, view ->
                dialog.dismiss()
            }
        }

        dialog?.show()
    }

    override fun vegOnlyClickListener(vegOnly: Boolean, nonVegOnly: Boolean) {
        foodAdapter.clear()

        addMealDetails(vegOnly, nonVegOnly)
        val item1 = TitleItem(title = "Todays Special", items = "230 ITEMS")

        foodAdapter.add(item1)

        if (vegOnly && !nonVegOnly) {
            addSpecialFoodItems(VEG)
        } else if (nonVegOnly && !vegOnly) {
            addSpecialFoodItems(NONVEG)
        } else {
            addSpecialFoodItems(ALL)
        }

        val item3 = TitleItem(title = "Snacks")
        foodAdapter.add(item3)

        if (vegOnly && !nonVegOnly) {
            addSnacksItems(VEG)
        } else if (nonVegOnly && !vegOnly) {
            addSnacksItems(NONVEG)
        } else {
            addSnacksItems(ALL)
        }

    }

    override fun onCartBarClick() {
        if (parentFragmentManager.findFragmentByTag(CartFragment.TAG) == null) {
            val fragment = CartFragment.newInstance(TAG)
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .add(R.id.fragmentContainer, fragment)
                .addToBackStack(CartFragment.TAG)
                .commit()
        }
    }

    private fun addSpecialFoodItems(vegStatus: Int? = null) {
        if (vegStatus == ALL) {
            foodList.forEach {
                foodAdapter.add(FoodItem(foodDto = it, callback = this))
            }
        } else {
            foodList.forEach {
                if (it.vegStatus == vegStatus) {
                    foodAdapter.add(FoodItem(foodDto = it, callback = this))
                }
            }
        }
    }

    private fun addSnacksItems(vegStatus: Int? = null) {
        if (vegStatus == ALL) {
            foodList.forEach {
                foodAdapter.add(FoodItem(foodDto = it, callback = this,hideThumbnail = true))
            }
        } else {
            foodList.forEach {
                if (it.vegStatus == vegStatus) {
                    foodAdapter.add(FoodItem(foodDto = it, callback = this,hideThumbnail = true))
                }
            }
        }
    }

    private fun addMealDetails(vegOnly: Boolean, nonVegOnly: Boolean) {
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

    override fun onIncreaseSearchItemClicked(foodDto: FoodDto) {
        for (i in 0 until foodAdapter.groupCount) {
            val group = foodAdapter.getGroupAtAdapterPosition(i)
            if (group is FoodItem && group.foodDto.id == foodDto.id) {
                group.foodDto.quantity = foodDto.quantity
                foodAdapter.notifyItemChanged(i, QuantityChangedPayload)
            }
        }
    }

    override fun onDecreaseSearchItemClicked(foodDto: FoodDto) {
        for (i in 0 until foodAdapter.groupCount) {
            val group = foodAdapter.getGroupAtAdapterPosition(i)
            if (group is FoodItem && group.foodDto.id == foodDto.id) {
                group.foodDto.quantity = foodDto.quantity
                foodAdapter.notifyItemChanged(i, QuantityChangedPayload)
            }
        }
    }
}