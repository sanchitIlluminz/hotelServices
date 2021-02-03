package com.illuminz.application.ui.food.items

import com.core.utils.GlideApp
import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food.view.*
import kotlinx.android.synthetic.main.item_food.view.ivImage
import kotlinx.android.synthetic.main.item_food.view.tvPrice

class FoodItem(
    private val image: String,
    private val title: String,
    private val price: Double,
    private val type: Int? = 0,
    private var quantity: Int = 0,
    val callback: Callback
) : Item(), AddMenuItemView.Callback {
    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(quantity, true)
        } else {
            super.bind(viewHolder, position, payloads)
        }
        callback.onDecreaseMenuItemClicked(quantity)

    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //type 0 is for veg and 1 is for non veg//

        viewHolder.itemView.apply {
            GlideApp.with(this)
                .load(image)
                .placeholder(R.color.colorPrimary)
                .error(R.color.black)
                .centerCrop()
                .into(ivImage)

            tvFoodTitle.text = title
            tvPrice.text = CurrencyFormatter.format(amount = price,currencyCode = "INR")
            quantityView.setCallback(this@FoodItem)
            quantityView.setQuantity(quantity, false)
            callback.onDecreaseMenuItemClicked(quantity)

            if(type==0){
                ivFoodType.setImageResource(R.drawable.ic_vegsymbol)
            }else{
                ivFoodType.setImageResource(R.drawable.ic_eggsymbol)
            }

//            btAdd.setOnClickListener {
//
//            }

//            if (countEmpty == true){
//                btAdd.visible()
//                llQuantity.gone()
//            }else{
//                btAdd.gone()
//                llQuantity.visible()
//
//            }
        }
    }

    override fun getLayout(): Int = R.layout.item_food


//    override fun onIncreaseMenuItemQuantityClicked() {
//        quantity += 1
//        notifyChanged(QuantityChangedPayload)
//    }
//
//    override fun onDecreaseMenuItemQuantityClicked() {
//        quantity -= 1
//        notifyChanged(QuantityChangedPayload)
//    }

    interface Callback {
        fun onIncreaseMenuItemClicked(count: Int)
        fun onDecreaseMenuItemClicked(count: Int)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        quantity += 1
        notifyChanged(QuantityChangedPayload)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        quantity -= 1
        notifyChanged(QuantityChangedPayload)
    }
}