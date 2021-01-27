package com.illuminz.application.ui.home.food.items

import com.illuminz.application.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food_offer_list.view.*

class FoodOfferListItem(
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

            val item = listOf(  FoodOfferItem(title = "BUY 1 GET 1", description = "FREE on starters & main course"),
                FoodOfferItem(title = "Flat 25% OFF", description = "Order & Get up to 25% off on Rs. 500 & above"),
                FoodOfferItem(title = "Combo Offer", description = "Enjoy our Biryani combo offers"),
                FoodOfferItem(title = "BUY 1 GET 1", description = "FREE on starters & main course")
            )

            val offerAdapter:GroupAdapter<GroupieViewHolder> = GroupAdapter()
            rvFoodOfferList.adapter = offerAdapter
            offerAdapter.addAll(item)
        }
    }

    override fun getLayout(): Int = R.layout.item_food_offer_list
}