package com.core.transition

import android.view.Gravity
import android.view.ViewGroup
import androidx.transition.*

class AppSlideTransition(@GravityFlag slideEdge: Int = Gravity.BOTTOM) : Slide(slideEdge) {
    private val propagation = AppSlidePropagation()

    override fun setPropagation(transitionPropagation: TransitionPropagation?) {
        super.setPropagation(propagation)
    }

    class AppSlidePropagation : VisibilityPropagation() {
        override fun getStartDelay(
            sceneRoot: ViewGroup?,
            transition: Transition?,
            startValues: TransitionValues?,
            endValues: TransitionValues?
        ): Long {
            return 0L
        }
    }
}