package com.core.extensions

import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder

fun GroupAdapter<GroupieViewHolder>.itemRange(): IntRange {
    val count = itemCount
    return if (count > 0) {
        0 until count
    } else {
        IntRange.EMPTY
    }
}

fun GroupAdapter<GroupieViewHolder>.groupRange(): IntRange {
    val count = groupCount
    return if (count > 0) {
        0 until count
    } else {
        IntRange.EMPTY
    }
}

fun GroupAdapter<GroupieViewHolder>.getGroup(predicate: (index: Int) -> Boolean): Group? {
    for (index in itemRange()) if (predicate(index)) return getItem(index)
    return null
}