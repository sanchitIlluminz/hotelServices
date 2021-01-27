package com.core.utils

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder

class LoopingGroupAdapter(private val count: Int) : GroupAdapter<GroupieViewHolder>() {
    override fun getItemCount(): Int {
        return if (count == 1) {
            count
        } else {
            Int.MAX_VALUE
        }
    }

    override fun getItem(position: Int): Item<*> {
        return super.getItem(
            if (count == 1) {
                position
            } else {
                position % count
            }
        )
    }
}