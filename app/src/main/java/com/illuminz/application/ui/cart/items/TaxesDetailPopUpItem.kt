package com.illuminz.application.ui.cart.items

import androidx.annotation.DrawableRes
import com.core.extensions.orZero
import com.illuminz.application.R
import com.illuminz.data.models.response.TaxesDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_taxes_detail_pop_up.view.*

class TaxesDetailPopUpItem(
    val taxes:TaxesDto
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTaxLabel.text = "${taxes.taxTitle}(${taxes.taxRate}%)"
            tvTaxes.text = CurrencyFormatter.format(taxes.debit.orZero())
        }
    }

    override fun getLayout(): Int = R.layout.item_taxes_detail_pop_up

}