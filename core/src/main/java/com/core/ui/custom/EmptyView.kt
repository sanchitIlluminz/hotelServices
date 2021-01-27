package com.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.core.R
import kotlinx.android.synthetic.main.layout_empty_view.view.*

class EmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {
    private var type = -1

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true)

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.EmptyView, defStyleAttr, 0)

        val imageResId = typedArray.getResourceId(R.styleable.EmptyView_emptyViewImage, -1)
        setImageResId(imageResId)

        val title = typedArray.getString(R.styleable.EmptyView_emptyViewTitle)
        setTitle(title)

        val message = typedArray.getString(R.styleable.EmptyView_emptyViewMessage)
        setMessage(message)

        val buttonVisible =
            typedArray.getBoolean(R.styleable.EmptyView_emptyViewButtonVisible, false)
        setButtonVisible(buttonVisible)

        val buttonText = typedArray.getString(R.styleable.EmptyView_emptyViewButtonText)
        setButtonText(buttonText)

        typedArray.recycle()
    }

    fun setImageResId(@DrawableRes resId: Int?) {
        if (resId == null || resId == -1) {
            ivImage.visibility = View.GONE
        } else {
            ivImage.visibility = View.VISIBLE
            ivImage.setImageResource(resId)
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

    fun setButtonVisible(visible: Boolean) {
        if (visible) {
            btnButton.visibility = View.VISIBLE
        } else {
            btnButton.visibility = View.GONE
        }
    }

    fun setButtonText(buttonText: String?) {
        btnButton.text = buttonText
    }

    fun setButtonClickListener(listener: OnClickListener?) {
        btnButton.setOnClickListener(listener)
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun getType(): Int = type
}