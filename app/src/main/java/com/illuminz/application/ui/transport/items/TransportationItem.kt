package com.illuminz.application.ui.transport.items

import com.core.utils.GlideApp
import com.illuminz.application.R
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_transportation.view.*

class TransportationItem(
    val image: String,
    val name:String,
    val price:Double,
    val distance:Double
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

            GlideApp.with(this)
                    .load(image)
                .placeholder(R.color.colorPrimary)
                .error(R.color.black)
                .centerCrop()
                .into(ivTransport)


            tvName.text = name
            val price = CurrencyFormatter.format(amount = price, currencyCode = "INR")

            "$price | $distance km".also { tvDistancePerKm.text = it }

            cdTransportItem.setOnClickListener {
                clTransportItem.callOnClick()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_transportation

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 1
    }
}