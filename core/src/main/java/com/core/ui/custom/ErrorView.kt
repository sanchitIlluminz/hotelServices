package com.core.ui.custom

import android.content.Context
import android.text.Spannable
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.core.R
import kotlinx.android.synthetic.main.layout_welcome_dialog.view.*


class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {
    private var errorButtonClickListener: ErrorButtonClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_welcome_dialog, this, true)

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ErrorView, defStyleAttr, 0)

        val errorDrawableResId = typedArray.getResourceId(R.styleable.ErrorView_errorDrawable, -1)
        if (errorDrawableResId != -1) {
            ivError.setImageResource(errorDrawableResId)
        }

        val title = typedArray.getString(R.styleable.ErrorView_errorTitle)
        setTitle(title)

        val message = typedArray.getString(R.styleable.ErrorView_errorMessage)
        setMessage(message)

        val errorButtonVisible =
            typedArray.getBoolean(R.styleable.ErrorView_errorButtonVisible, false)
        if (errorButtonVisible) {
            btnError.visibility = View.VISIBLE
        } else {
            btnError.visibility = View.GONE
        }

        val errorButtonText = typedArray.getString(R.styleable.ErrorView_errorButtonText)
        btnError.text = errorButtonText

        typedArray.recycle()

        btnError.setOnClickListener {
            errorButtonClickListener?.onErrorButtonClicked()
        }
    }

    fun setTitle(title: String?) {
        if (title.isNullOrBlank()) {
            tvTitle.visibility = View.GONE
        } else {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = title
        }
    }

    fun setMessage(message: String?) {
        if (message.isNullOrBlank()) {
            tvMessage.visibility = View.GONE
        } else {
            tvMessage.visibility = View.VISIBLE
            tvMessage.text = message
        }
    }

    fun setMessage(message: String?, spanText: String, imageId: Int) {
        if (message.isNullOrBlank()) {
            tvMessage.visibility = View.GONE
        } else {
            tvMessage.visibility = View.VISIBLE
            // tvMessage.text = message

            val index = message.indexOf(spanText)
            val lengthOfSpanText = spanText.length

            val span = Spannable.Factory.getInstance().newSpannable(message)

            span.setSpan(
                ImageSpan(context, imageId),
                index, index + lengthOfSpanText,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvMessage.text = span

            //android.R.drawable.star_on)
        }
    }

    fun setFullWidth(){
        btnError.layoutParams.width=LayoutParams.MATCH_PARENT
    }

    fun setDrawable(drawable:Int){
        ivError.setImageResource(drawable)
    }

    fun setErrorButtonClickListener(listener: ErrorButtonClickListener) {
        this.errorButtonClickListener = listener
    }

    interface ErrorButtonClickListener {
        fun onErrorButtonClicked()
    }
}