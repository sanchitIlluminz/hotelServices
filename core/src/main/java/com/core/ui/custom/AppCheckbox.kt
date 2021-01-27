package com.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Checkable
import android.widget.FrameLayout
import com.core.R
import kotlinx.android.synthetic.main.layout_app_checkbox.view.*

class AppCheckbox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Checkable {
    private var checked = false
    private var checkChangedListener: OnCheckedChangeListener? = null
    private var animateDuration = 150L

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_app_checkbox, this, true)

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.AppCheckbox, defStyleAttr, 0)

        val backgroundResId =
            typedArray.getResourceId(R.styleable.AppCheckbox_appCheckboxBackground, -1)
        setCheckboxBackground(backgroundResId)

        val foregroundResId =
            typedArray.getResourceId(R.styleable.AppCheckbox_appCheckboxForeground, -1)
        setCheckboxForeground(foregroundResId)

        val checkboxChecked =
            typedArray.getBoolean(R.styleable.AppCheckbox_appCheckboxChecked, false)
        isChecked = checkboxChecked

        animateDuration =
            typedArray.getInt(R.styleable.AppCheckbox_appCheckboxAnimateDuration, 150).toLong()

        typedArray.recycle()

        flCheckboxBackground.setOnClickListener { toggle() }
    }

    private fun setCheckboxBackground(resId: Int) {
        flCheckboxBackground.setBackgroundResource(resId)
    }

    private fun setCheckboxForeground(resId: Int) {
        ivCheckbox.setImageResource(resId)
    }

    override fun isChecked(): Boolean = checked

    override fun toggle() {
        isChecked = !checked
    }

    override fun setChecked(checked: Boolean) {
        setChecked(checked, true)
    }

    fun setChecked(checked: Boolean, animate: Boolean = true) {
        this.checked = checked
        checkChangedListener?.onCheckedChanged(checked)

        val scaleXY: Float = if (checked) {
            1f
        } else {
            0f
        }

        if (isInEditMode || !animate) {
            ivCheckbox.scaleX = scaleXY
            ivCheckbox.scaleY = scaleXY
        } else {
            ivCheckbox.animate()
                .scaleX(scaleXY)
                .scaleY(scaleXY)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(animateDuration)
                .start()
        }
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        this.checkChangedListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        flCheckboxBackground.setOnClickListener(null)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        flCheckboxBackground.isEnabled = enabled
        flCheckboxBackground.isClickable = enabled
        flCheckboxBackground.isFocusable = enabled
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(isChecked: Boolean)
    }
}