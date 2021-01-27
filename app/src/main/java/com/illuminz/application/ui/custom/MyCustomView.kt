package com.illuminz.application.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.illuminz.application.R

class MyCustomView(context: Context, attr: AttributeSet?=null): View(context, attr) {


    init {
//        LayoutInflater.from(context).inflate(R.layout.custom_view, this, true)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        paint.setColor(Color.GRAY)
//        paint.strokeWidth = 4F
//        paint.style = Paint.Style.STROKE
//
//        val paint1 = Paint(Paint.ANTI_ALIAS_FLAG)
//        paint1.setColor(Color.RED)
//        paint1.strokeWidth = 4F
//        paint1.style = Paint.Style.FILL_AND_STROKE
//
//        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
//        paint2.setColor(Color.GREEN)
//        paint2.strokeWidth = 4F
//        paint2.style = Paint.Style.FILL_AND_STROKE
//
//        canvas?.drawLine(0F,0F,250F,250F,paint)
//
//        canvas?.drawCircle(50F, 50F, 10F,paint1)
//
//        canvas?.drawRect(100F,100F,200F,200F,paint2)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(200,200)
    }
}