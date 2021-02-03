package com.illuminz.application.ui.food

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import com.core.extensions.dpToPx
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.illuminz.application.R
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.food.items.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_menu.*
import kotlinx.android.synthetic.main.fragment_food_list.*


class FoodListFragment : DaggerBaseFragment(), FoodItem.Callback, FoodListItem.Callback,
    VegNonVegItem.Callback, CartBarView.Callback {
    companion object {
        const val TAG = "FoodListFragment"
        fun newInstance(): FoodListFragment {
            return FoodListFragment()
        }
    }

    private lateinit var foodAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var menuList: List<MenuDialogItem>
    private var totalPrice: Double = 0.00
    private lateinit var item: FoodOfferListItem

    override fun getLayoutResId() = R.layout.fragment_food_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        btnMenu.setOnClickListener {
            showMenuDialog(menuList)
        }

        cartBarView.setCallback(this)
    }

    private fun initialise() {


        cartBarView.setButtonText(getString(R.string.view_cart))
        cartBarView.setItemPrice(totalPrice = 820.00, items = 4)

        foodAdapter = GroupAdapter()
        rvFood.adapter = foodAdapter

//        val layoutManager = rvFood.layoutManager as GridLayoutManager
//        layoutManager.spanSizeLookup = foodAdapter.spanSizeLookup

        val item00 = FoodTimingItem()
        item = FoodOfferListItem()
        val item0 = VegNonVegItem(isVegOnly = true, isNonVegOnly = false, callback = this)
        val item1 = TitleItem(title = "Todays Special", items = "230 ITEMS")

        val item2 = getFoodItem(0)

        val item3 = TitleItem(title = "Snacks")

        val item4 = getFoodListItem(0)

        foodAdapter.add(item00)
        foodAdapter.add(item)
        foodAdapter.add(item0)
        foodAdapter.add(item1)
        foodAdapter.addAll(item2)
        foodAdapter.add(item3)
        foodAdapter.addAll(item4)


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

    override fun onIncreaseMenuItemClicked(count: Int) {
        var quantity = count
    }

    override fun onDecreaseMenuItemClicked(count: Int) {
        var quantity = count
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

        val item00 = FoodTimingItem()

        val item0 = VegNonVegItem(vegOnly, nonVegOnly, this)
        foodAdapter.add(item00)
        foodAdapter.add(item)
        foodAdapter.add(item0)

        val item1 = TitleItem(title = "Todays Special", items = "230 ITEMS")
        val item3 = TitleItem(title = "Snacks")

        if (vegOnly == true && nonVegOnly == false) {
            val item2 = getFoodItem(0)
            val item4 = getFoodListItem(0)

            foodAdapter.add(item1)
            foodAdapter.addAll(item2)
            foodAdapter.add(item3)
            foodAdapter.addAll(item4)
        } else if (nonVegOnly == true && vegOnly == false) {
            val item2 = getFoodItem(1)
            val item4 = getFoodListItem(1)

            foodAdapter.add(item1)
            foodAdapter.addAll(item2)
            foodAdapter.add(item3)
            foodAdapter.addAll(item4)
        } else {
            val item2 = getFoodItem(0) + getFoodItem(1)
            val item4 = getFoodListItem(0) + getFoodListItem(1)

            foodAdapter.add(item1)
            foodAdapter.addAll(item2)
            foodAdapter.add(item3)
            foodAdapter.addAll(item4)
        }

    }


    private fun getFoodItem(type: Int): List<FoodItem> {
        //type 1 for veg else nonveg//
        if (type == 0) {
            return listOf(
                FoodItem(
                    image = "https://www.seriouseats.com/2020/06/20200602-western-denver-omelette-daniel-gritzer-8.jpg",
                    title = "Sunny Side Up Omelete with Roasted Breads",
                    price = 200.00,
                    quantity = 2,
                    callback = this
                ),
                FoodItem(
                    image = "https://static.toiimg.com/thumb/62400098.cms?imgsize=462916&width=800&height=800",
                    title = "Stuffed Pranthas with Butter - 4 Pc",
                    price = 300.00,
                    callback = this
                ),
                FoodItem(
                    image = "https://preppykitchen.com/wp-content/uploads/2019/08/Pancakes-recipe-1200.jpg",
                    title = "Chocolate and Strawberry Pancake",
                    price = 500.00,
                    quantity = 2,
                    callback = this
                ),
                FoodItem(
                    image = "https://static.toiimg.com/thumb/54714340.cms?imgsize=458099&width=800&height=800",
                    title = "Vegetable Grilled Sandwich - 4 Pc",
                    price = 100.00,
                    callback = this
                )
            )
        } else {
            return listOf(
                FoodItem(
                    image = "https://static.toiimg.com/thumb/62400098.cms?imgsize=462916&width=800&height=800",
                    title = "Stuffed Pranthas with Butter - 4 Pc",
                    price = 300.00,
                    callback = this,
                    type = 1
                ),
                FoodItem(
                    image = "https://preppykitchen.com/wp-content/uploads/2019/08/Pancakes-recipe-1200.jpg",
                    title = "Chocolate and Strawberry Pancake",
                    price = 500.00,
                    quantity = 2,
                    callback = this,
                    type = 1
                ),
                FoodItem(
                    image = "https://www.seriouseats.com/2020/06/20200602-western-denver-omelette-daniel-gritzer-8.jpg",
                    title = "Sunny Side Up Omelete with Roasted Breads",
                    price = 200.00,
                    quantity = 2,
                    callback = this,
                    type = 1
                ),
                FoodItem(
                    image = "https://static.toiimg.com/thumb/54714340.cms?imgsize=458099&width=800&height=800",
                    title = "Vegetable Grilled Sandwich - 4 Pc",
                    price = 100.00,
                    callback = this,
                    type = 1
                )
            )
        }
    }


    private fun getFoodListItem(type: Int): List<FoodListItem> {
        if (type == 0) {
            return listOf(
                FoodListItem(
                    title = "Sunny Side Up Omelete with Roasted Breads",
                    price = 200.00,
                    quantity = 0,
                    callback = this
                ),
                FoodListItem(
                    title = "Stuffed Pranthas with Butter - 4 Pc",
                    price = 200.00,
                    quantity = 2,
                    callback = this
                ),
                FoodListItem(
                    title = "Chocolate and Strawberry Pancake",
                    price = 200.00,
                    quantity = 0,
                    callback = this
                ),
                FoodListItem(
                    title = "Vegetable Grilled Sandwich - 4 Pc",
                    price = 200.00,
                    quantity = 1,
                    callback = this
                ),
                FoodListItem(
                    title = "Samosa with Chana and Chutney",
                    price = 200.00,
                    quantity = 0,
                    callback = this
                ),
                FoodListItem(
                    title = "Spicy Paneer Tikka",
                    price = 200.00,
                    quantity = 3,
                    callback = this
                )
            )
        } else {
            return listOf(
                FoodListItem(
                    title = "Vegetable Grilled Sandwich - 4 Pc",
                    price = 200.00,
                    quantity = 1,
                    callback = this,
                    veg = false
                ),
                FoodListItem(
                    title = "Samosa with Chana and Chutney",
                    price = 200.00,
                    quantity = 0,
                    callback = this,
                    veg = false
                ),
                FoodListItem(
                    title = "Spicy Paneer Tikka",
                    price = 200.00,
                    quantity = 3,
                    callback = this,
                    veg = false
                )
            )
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
}