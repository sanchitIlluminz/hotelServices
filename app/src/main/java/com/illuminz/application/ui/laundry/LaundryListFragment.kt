package com.illuminz.application.ui.laundry

import android.os.Bundle
import android.view.View
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.food.items.FoodListItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_laundry_list.*

class LaundryListFragment : DaggerBaseFragment(), FoodListItem.Callback {
    companion object {
        const val TAG = "LaundryListFragment"
        fun newInstance() : LaundryListFragment{
            return LaundryListFragment()
        }
    }

    private lateinit var listAdapter: GroupAdapter<GroupieViewHolder>

    override fun getLayoutResId(): Int = R.layout.fragment_laundry_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()


    }

    private fun initialise() {
        listAdapter = GroupAdapter()
        rvLaundry.adapter = listAdapter

        val item = listOf(
                        FoodListItem(title = "Shirt",price = 100.00,quantity = 1,callback = this,foodItem = false),
                        FoodListItem(title = "T-Shirt",price = 150.00,callback = this,foodItem = false),
                        FoodListItem(title = "Jeans",price = 250.00,quantity = 1,callback = this,foodItem = false),
                        FoodListItem(title = "Trousers",price = 350.00,callback = this,foodItem = false),
                        FoodListItem(title = "kurta",price = 150.00,quantity = 1,callback = this,foodItem = false),
                        FoodListItem(title = "Blazer",price = 250.00,callback = this,foodItem = false),
                        FoodListItem(title = "Saree",price = 150.00,quantity = 1,callback = this,foodItem = false),
                        FoodListItem(title = "Pyjama",price = 950.00,callback = this,foodItem = false),
                        FoodListItem(title = "Jacket",price = 150.00,quantity = 1,callback = this,foodItem = false),
                        FoodListItem(title = "Shirt",price = 100.00,quantity = 1,callback = this,foodItem = false),
                        FoodListItem(title = "T-Shirt",price = 150.00,callback = this,foodItem = false),
                        FoodListItem(title = "Jeans",price = 250.00,quantity = 1,callback = this,foodItem = false),
                        FoodListItem(title = "Trousers",price = 350.00,callback = this,foodItem = false)
        )

        listAdapter.addAll(item)



    }

    override fun onIncreaseMenuItemClicked(count: Int) {
        var quantity =count
    }

    override fun onDecreaseMenuItemClicked(count: Int) {
        var quantity =count
    }
}