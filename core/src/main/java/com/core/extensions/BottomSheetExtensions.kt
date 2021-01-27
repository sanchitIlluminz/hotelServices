package com.core.extensions

import com.google.android.material.bottomsheet.BottomSheetBehavior

fun BottomSheetBehavior<*>.expand() {
    if (state != BottomSheetBehavior.STATE_EXPANDED) {
        state = BottomSheetBehavior.STATE_EXPANDED
    }
}

fun BottomSheetBehavior<*>.collapse() {
    if (state != BottomSheetBehavior.STATE_COLLAPSED) {
        state = BottomSheetBehavior.STATE_COLLAPSED
    }
}