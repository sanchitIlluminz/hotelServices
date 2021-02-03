package com.illuminz.application.ui.massage.items

import com.core.extensions.gone
import com.core.extensions.visible
import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.utils.QuantityChangedPayload
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.items_massage.view.*
import kotlinx.android.synthetic.main.items_massage.view.quantityView
import kotlinx.android.synthetic.main.items_massage.view.tvTitle

class MassageItems(
    private val title: String,
    private val personCount: Int,
    private val duration: String,
    private var quantity: Int = 0,
    val callback: Callback,
    var lastItem: Boolean = false
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
        viewHolder.itemView.apply {
            tvTitle.text = title
            (context.getString(R.string.valid_for) + "$personCount" + context.getString(R.string.person))
                .also { tvPersonCount.text = it }

            ("Duration: " + "$duration").also { tvDaysCount.text = it }

            quantityView.setCallback(callback = this@MassageItems)
            quantityView.setQuantity(quantity, false)

            if (lastItem)
                ivDivider.gone()
            else
                ivDivider.visible()
        }
    }

    interface Callback {
        fun onIncreaseMenuItemClicked(count: Int)
        fun onDecreaseMenuItemClicked(count: Int)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        quantity = quantity.plus(1)
        notifyChanged(QuantityChangedPayload)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        quantity = quantity.minus(1)
        notifyChanged(QuantityChangedPayload)
    }

    override fun getLayout(): Int = R.layout.items_massage
}