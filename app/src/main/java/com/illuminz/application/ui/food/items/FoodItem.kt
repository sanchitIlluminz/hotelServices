package com.illuminz.application.ui.food.items

import com.core.extensions.gone
import com.core.extensions.orZero
import com.core.extensions.visible
import com.core.utils.GlideApp
import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food.view.*
import kotlinx.android.synthetic.main.item_food.view.ivImage
import kotlinx.android.synthetic.main.item_food.view.tvPrice

class FoodItem(
    var serviceCategoryItem: ServiceCategoryItemDto,
    val callback: Callback,
    private var remark: String? = null,
    private val hideThumbnail: Boolean? = false
) : Item(), AddMenuItemView.Callback {
    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(
                serviceCategoryItem.quantity.orZero(),
                true
            )
        } else {
            super.bind(viewHolder, position, payloads)
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //type 0 is for veg and 1 is for non veg//
        viewHolder.itemView.apply {
            if (serviceCategoryItem.image != null) {
                ivImage.visible()

                GlideApp.with(this)
                    .load(serviceCategoryItem.image.orEmpty())
                    .placeholder(R.color.colorPrimary)
                    .error(R.color.black)
                    .centerCrop()
                    .into(ivImage)
            } else {
                ivImage.gone()
            }

            if (hideThumbnail == true) {
                ivImage.gone()
                tvFoodRemark.gone()
            }

            tvFoodTitle.text = serviceCategoryItem.itemName.orEmpty()
            tvPrice.text = CurrencyFormatter.format(
                amount = serviceCategoryItem.price?.toDouble().orZero(),
                currencyCode = "INR"
            )
            quantityView.setCallback(this@FoodItem)
            quantityView.setQuantity(serviceCategoryItem.quantity.orZero(), false)
//            callback.onDecreaseMenuItemClicked(this@FoodItem)

            if (serviceCategoryItem.vegStatus == 1) {
                ivFoodType.setImageResource(R.drawable.ic_vegsymbol)
            } else {
                ivFoodType.setImageResource(R.drawable.ic_eggsymbol)
            }

            if (remark.isNullOrBlank()) {
                tvFoodRemark.gone()
            } else {
                tvFoodRemark.visible()
                tvFoodRemark.text = remark
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_food

    interface Callback {
        fun onIncreaseFoodItemClicked(foodItem: FoodItem)
        fun onDecreaseFoodItemClicked(foodItem: FoodItem)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        serviceCategoryItem.quantity += 1
        notifyChanged(QuantityChangedPayload)
        callback.onIncreaseFoodItemClicked(this)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        serviceCategoryItem.quantity -= 1
        notifyChanged(QuantityChangedPayload)
        callback.onDecreaseFoodItemClicked(this)
    }
}