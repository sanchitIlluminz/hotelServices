package com.illuminz.application.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.illuminz.application.R
import com.illuminz.data.utils.CurrencyFormatter
import kotlinx.android.synthetic.main.item_cart_bar_view.view.*

class CartBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr){
    private var callback: Callback? = null
    private var background: Int = 0
    private var textColor: Int = 0
    init {
        LayoutInflater.from(context).inflate(R.layout.item_cart_bar_view,this,true)

        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.CartBarView)

        background = typedArray.getResourceId(R.styleable.CartBarView_barBackground,-1)
        textColor = typedArray.getColor(R.styleable.CartBarView_textColor,-1)

        if (background!= -1) {
            cartBar.setBackgroundResource(background)
        }
        if (textColor!= -1){
            tvItem.setTextColor(textColor)
        }


        typedArray.recycle()

        if (!isInEditMode){
            cartBar.setOnClickListener {
//                it.performHapticFeedback()
                callback?.onCartBarClick()
            }
        }
    }

    fun setItemPrice(totalPrice:Double,items:Int){
        tvItem.text = resources.getQuantityString(R.plurals.cart_items, items, items) +"  |  " +
                CurrencyFormatter.format(amount = totalPrice, currencyCode = "INR")
    }

    fun setButtonText(text:String){
        tvViewCart.text = text
    }



    interface Callback{
        fun onCartBarClick()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

}