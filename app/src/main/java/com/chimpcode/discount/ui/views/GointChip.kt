package com.chimpcode.discount.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.chimpcode.discount.R
import com.chimpcode.discount.data.GointCategory


/**
 * Created by anargu on 11/18/17.
 */
class GointChip : TextView {

    val TAG = "GointChip"

    private val DEFAULT_TYPE_CHIP = 0
    val CHIP_SELECTED = 1
    val CHIP_AVAILABLE= 0
    private var ctx : Context? = null
    private var activeColor : Int = Color.parseColor("#C12B35")
    private var whiteColor : Int = Color.parseColor("#FFFFFF")
    var categoryData : GointCategory? = null

    private var blankBackground : Drawable = ContextCompat.getDrawable(context, R.drawable.shape_chip_simple_drawable_available)!!
    private var primaryBackground : Drawable = ContextCompat.getDrawable(context, R.drawable.shape_chip_simple_drawable_selected)!!

    var type_chip = CHIP_AVAILABLE

    constructor(ctx : Context) : super(ctx) {
        this.ctx = ctx
        init()
    }
    constructor(ctx : Context, attrs : AttributeSet) : super(ctx, attrs) {
        this.ctx = ctx
        init(attrs)
    }
    constructor(ctx : Context, attrs : AttributeSet, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        this.ctx = ctx
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        if ( attrs != null ) {
            val params: TypedArray = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.GointChip,
                    defStyleAttr, 0)
            val typeChip = params.getInt(R.styleable.GointChip_chip_type, DEFAULT_TYPE_CHIP)
            val chipText = params.getString(R.styleable.GointChip_chip_text)

            setText(chipText)
        } else {
            this.background = blankBackground
            setText("")
        }

        val chipLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        chipLayoutParams.setMargins(10,10,10,10)
        this.layoutParams = chipLayoutParams
    }

    fun changeChipType () {
        if (this.type_chip == CHIP_AVAILABLE) {
            this.type_chip = CHIP_SELECTED
        } else {
            this.type_chip = CHIP_AVAILABLE
        }
    }

    fun setText(value : String) {
        this.text = value
        val typeface = Typeface.createFromAsset(context.assets, "fonts/nexa_bold.otf")
        this.typeface = typeface
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
    }

    fun setColor(color: Int) {
        activeColor = ContextCompat.getColor(ctx!!, color)

        if (type_chip == CHIP_AVAILABLE) {
            this.setTextColor(activeColor)
        } else {
            this.setTextColor(whiteColor)
        }

        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_chip_simple_drawable_selected) as GradientDrawable
        backgroundDrawable.setColor(activeColor)
        primaryBackground = backgroundDrawable

        val backgroundBlankDrawable = ContextCompat.getDrawable(context, R.drawable.shape_chip_simple_drawable_available) as GradientDrawable
        backgroundBlankDrawable.setStroke(4, activeColor)
        blankBackground = backgroundBlankDrawable

        if (type_chip == CHIP_SELECTED) {
            this.background = primaryBackground
        } else {
            this.background = blankBackground
        }
    }

    fun changeStyleAtClick() {
        changeChipType()
        if ( type_chip == CHIP_AVAILABLE ) {
            this.background = blankBackground
            this.setTextColor(activeColor)
        } else {
            this.background = primaryBackground
            this.setTextColor(whiteColor)
        }
    }

    fun turnOnChip () {
        this.background = primaryBackground
        this.setTextColor(whiteColor)
        this.type_chip = CHIP_SELECTED
    }

    fun turnOffChip() {
        this.type_chip = CHIP_AVAILABLE
        this.background = blankBackground
        this.setTextColor(activeColor)
    }
}