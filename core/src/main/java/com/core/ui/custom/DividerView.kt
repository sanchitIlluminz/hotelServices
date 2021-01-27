package com.core.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.core.R

class DividerView : View {
    companion object {
        private const val ORIENTATION_HORIZONTAL = 0
        private const val ORIENTATION_VERTICAL = 1
    }

    private val paint: Paint
    private val orientation: Int

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DividerView, defStyle, 0)
        val dashGap = typedArray.getDimensionPixelSize(R.styleable.DividerView_dashGap, 5).toFloat()
        val dashLength =
            typedArray.getDimensionPixelSize(R.styleable.DividerView_dashLength, 5).toFloat()
        val dashThickness =
            typedArray.getDimensionPixelSize(R.styleable.DividerView_dashThickness, 3).toFloat()
        val color = typedArray.getColor(R.styleable.DividerView_color, Color.BLACK)
        orientation = typedArray.getInt(
            R.styleable.DividerView_orientation,
            ORIENTATION_HORIZONTAL
        )
        typedArray.recycle()

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.apply {
            setColor(color)
            style = Paint.Style.STROKE
            strokeWidth = dashThickness
            pathEffect = DashPathEffect(floatArrayOf(dashLength, dashGap), 0f)
        }

        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (orientation == ORIENTATION_HORIZONTAL) {
            val center = height * 0.5f
            canvas.drawLine(0f, center, width.toFloat(), center, paint)
        } else {
            val center = width * 0.5f
            canvas.drawLine(center, 0f, center, height.toFloat(), paint)
        }
    }
}