package com.core.extensions

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

fun RecyclerView.canScrollBottom(): Boolean = canScrollVertically(1)

fun RecyclerView.cannotScrollBottom(): Boolean = !canScrollBottom()

fun RecyclerView.supportsChangeAnimations(supportsChangeAnimations: Boolean) {
    (itemAnimator as? SimpleItemAnimator?)?.supportsChangeAnimations = supportsChangeAnimations
}