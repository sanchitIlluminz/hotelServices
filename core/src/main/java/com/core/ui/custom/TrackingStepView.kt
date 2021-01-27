package com.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.core.R
import kotlinx.android.synthetic.main.layout_order_tracking_step.view.*

class TrackingStepView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_order_tracking_step, this, true)
    }

    fun update(@DrawableRes iconResId: Int, title: String, subTitle: String?) {
        ivIcon.setImageResource(iconResId)
        tvTitle.text = title

        tvSubTitle.isVisible = !subTitle.isNullOrBlank()
        tvSubTitle.text = subTitle
    }
}