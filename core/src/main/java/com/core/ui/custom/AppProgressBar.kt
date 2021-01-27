package com.core.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.core.R

class AppProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paintTotal = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG)

    private var totalValue: Int = 100
    private var totalColor: Int = 0

    private var progressValue: Int = 0
    private var progressColor: Int = 0

    private var animateDuration = 250L

    private var progressAnimator: ValueAnimator? = null
    private val progressInterpolator by lazy { LinearInterpolator() }

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppProgressBar)

            totalValue =
                typedArray.getInt(R.styleable.AppProgressBar_appProgressBar_totalValue, 100)
            totalColor =
                typedArray.getColor(R.styleable.AppProgressBar_appProgressBar_totalColor, 0)

            progressValue =
                typedArray.getInt(R.styleable.AppProgressBar_appProgressBar_progressValue, 0)
            progressColor =
                typedArray.getColor(R.styleable.AppProgressBar_appProgressBar_progressColor, 0)

            animateDuration =
                typedArray.getInt(R.styleable.AppProgressBar_appProgressBar_animateDuration, 250)
                    .toLong()

            typedArray.recycle()
        }

        paintTotal.style = Paint.Style.STROKE
        paintTotal.color = totalColor
        paintTotal.strokeCap = Paint.Cap.SQUARE

        paintProgress.style = Paint.Style.STROKE
        paintProgress.color = progressColor
        paintProgress.strokeCap = Paint.Cap.SQUARE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintTotal.strokeWidth = h.toFloat()
        paintProgress.strokeWidth = h.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val halfHeight = height.toFloat() / 2

        // Draw the total line
        canvas.drawLine(0f, halfHeight, width, halfHeight, paintTotal)

        // Draw the progress line
        val currentProgressX = (progressValue.toFloat() / totalValue.toFloat()) * width
        canvas.drawLine(0f, halfHeight, currentProgressX, halfHeight, paintProgress)
    }

    fun setProgress(progress: Int, animate: Boolean) {
        val validProgress = getValidProgressValue(progress)

        if (animate) {
            // Cancel any on-going animation
            progressAnimator?.cancel()

            val animator = ValueAnimator.ofInt(this.progressValue, validProgress)
            animator.interpolator = progressInterpolator
            animator.duration = animateDuration
            animator.addUpdateListener {
                // To make sure progress is valid in case of interpolator like "anticipate overshoot"
                this.progressValue = getValidProgressValue(it.animatedValue as Int)
                invalidate()
            }
            animator.start()
            progressAnimator = animator
        } else {
            this.progressValue = validProgress
            invalidate()
        }
    }

    /**
     * Filter out any invalid progress value
     * */
    private fun getValidProgressValue(input: Int): Int {
        return when {
            input < 0 -> 0
            input > totalValue -> totalValue
            else -> input
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressAnimator?.cancel()
    }
}