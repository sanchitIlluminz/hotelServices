package com.core.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.core.R


class VerticalProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paintTotal = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {

    }

    private var totalValue: Int = 100
    private var totalColor: Int = 0
    private var totalWidth: Float = 4f

    private var progressValue: Int = 0
    private var progressColor: Int = 0
    private var progressWidth: Float = 16f

    private var progressAnimator: ValueAnimator? = null
    private val progressInterpolator by lazy { AccelerateDecelerateInterpolator() }
    private val animateDuration = 1000L

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalProgressView)

            totalValue = typedArray.getInt(R.styleable.VerticalProgressView_totalValue, 100)
            totalColor = typedArray.getColor(R.styleable.VerticalProgressView_totalColor, 0)

            progressValue = typedArray.getInt(R.styleable.VerticalProgressView_progressValue, 0)
            progressColor = typedArray.getColor(R.styleable.VerticalProgressView_progressColor, 0)

            typedArray.recycle()
        }

        
        paintTotal.style = Paint.Style.STROKE
        paintTotal.color = totalColor
        paintTotal.strokeWidth = totalWidth
        paintTotal.strokeCap = Paint.Cap.SQUARE
        val interval=FloatArray(2){5f;10f}
        paintTotal.pathEffect = DashPathEffect(interval, 24F)

        paintProgress.style = Paint.Style.STROKE
        paintProgress.color = progressColor
        paintProgress.strokeWidth = progressWidth
        paintProgress.strokeCap = Paint.Cap.SQUARE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val height = height.toFloat()
        val width = width.toFloat()

        // Draw the total line
        canvas.drawLine(0f, 0f, width, height, paintTotal)


        // Draw the progress line
        val progressHeight = (progressValue.toFloat() / totalValue.toFloat()) * height
        canvas.drawLine(0f, 0f, width, progressHeight, paintProgress)
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
}