package com.core.ui.custom

import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMarginsRelative
import com.core.R
import com.core.extensions.dpToPx
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_divider.view.*

class DividerItem(
    @ColorRes private val colorResId: Int = R.color.transparent,
    private val thicknessDp: Int = 1,
    private val marginStartPx: Int = 0,
    private val marginEndPx: Int = 0,
    private val marginTopPx: Int = 0,
    private val marginBottomPx: Int = 0
) : Item() {
    override fun isLongClickable(): Boolean = false

    override fun isClickable(): Boolean = false

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            divider.setBackgroundColor(ContextCompat.getColor(context, colorResId))
            divider.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                height = context.dpToPx(thicknessDp)
                updateMarginsRelative(marginStartPx, marginTopPx, marginEndPx, marginBottomPx)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_divider
}