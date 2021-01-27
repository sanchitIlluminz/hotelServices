package com.illuminz.application.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.core.extensions.gone
import com.core.extensions.performHapticFeedback
import com.core.extensions.visible
import com.illuminz.application.R
import kotlinx.android.synthetic.main.layout_add_menu_item.view.*

class AddMenuItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var quantity: Int = 0
    private var callback: Callback? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_add_menu_item, this, true)

        if (!isInEditMode) {
            setQuantity(quantity, false)

            quantityTicker.typeface = ResourcesCompat.getFont(context, R.font.inter_bold)

            flAddText.setOnClickListener {
                if (quantity == 0) {
                    it.performHapticFeedback()
                    callback?.onIncreaseMenuItemQuantityClicked()
                }
            }

            ivAdd.setOnClickListener {
                it.performHapticFeedback()
                callback?.onIncreaseMenuItemQuantityClicked()
            }

            ivRemove.setOnClickListener {
                it.performHapticFeedback()
                callback?.onDecreaseMenuItemQuantityClicked()
            }
        }
    }

    fun setQuantity(
        quantity: Int,
        animate: Boolean
    ) {
        if (this.quantity != quantity && (quantity == 0 || quantity == 1)) {
            TransitionManager.beginDelayedTransition(rlRoot,
                AutoTransition().apply { duration = 150 })
        }

        if (quantity <= 0) {
            ivAdd.isEnabled = false
            ivRemove.isEnabled = false
            rlRoot.isEnabled = true

            this.quantity = 0
            quantityTicker.text = quantity.toString()

            ivAdd.gone()
            ivRemove.gone()
            flAddText.visible()
            quantityTicker.gone()
        } else {
            ivAdd.isEnabled = true
            ivRemove.isEnabled = true
            rlRoot.isEnabled = false

            this.quantity = quantity
            quantityTicker.setText(quantity.toString(), animate)

            ivAdd.visible()
            ivRemove.visible()
            flAddText.gone()
            quantityTicker.visible()
        }


    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onIncreaseMenuItemQuantityClicked()
        fun onDecreaseMenuItemQuantityClicked()
    }

    fun test(a:Int,b:Int,c:String?=null){}
}