package com.illuminz.application.ui.transport.items

import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_transport_title.view.*


class TransportTitleItem(
    private val title: String
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
                val spannable = SpannableString(title)

                spannable.setSpan(
                    UnderlineSpan(),
                    50,
                    61,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Toast.makeText(view.context, "Clicked!", Toast.LENGTH_SHORT).show()
                }
            }

            spannable.setSpan(
                clickableSpan,
                50,
                61,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.tab_highlight)),
                50,
                61,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            tvTitle.text = spannable
            tvTitle.movementMethod = LinkMovementMethod.getInstance()
            }
    }

    override fun getLayout(): Int = R.layout.item_transport_title

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 3
    }
}