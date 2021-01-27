package com.core.utils

sealed class AnimationDirection {
    object End : AnimationDirection()
    object Bottom : AnimationDirection()
    object None : AnimationDirection()
}