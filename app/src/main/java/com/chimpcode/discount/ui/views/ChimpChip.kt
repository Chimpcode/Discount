package com.chimpcode.discount.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.chimpcode.discount.R
import org.w3c.dom.Text

/**
 * Created by anargu on 11/18/17.
 */
class ChimpChip : TextView {

    private val DEFAULT_TYPE_CHIP = 0
    private val CHIP_SELECTED = 1
    private val CHIP_AVAILABLE= 0

    constructor(ctx : Context) : super(ctx) {
        init()
    }
    constructor(ctx : Context, attrs : AttributeSet) : super(ctx, attrs) {
        init(attrs)
    }
    constructor(ctx : Context, attrs : AttributeSet, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        if ( attrs != null ) {
            val params: TypedArray = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.ChimpChip,
                    defStyleAttr, 0)
            val typeChip = params.getInt(R.styleable.ChimpChip_chip_type, DEFAULT_TYPE_CHIP)
            val chipText = params.getString(R.styleable.ChimpChip_chip_text)

            if (typeChip == CHIP_SELECTED) {
                this.background = ContextCompat.getDrawable(context, R.drawable.shape_chip_simple_drawable_selected)
            } else {
                this.background = ContextCompat.getDrawable(context, R.drawable.shape_chip_simple_drawable_available)
            }
            setText(chipText, typeChip)
        } else {
            this.background = ContextCompat.getDrawable(context, R.drawable.shape_chip_simple_drawable_available)
            setText("")
        }
    }

    fun setText(value : String, type_chip: Int = CHIP_AVAILABLE) {
        this.text = value
        if ( type_chip != CHIP_SELECTED ) {
            this.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
            this.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        val typeface = Typeface.createFromAsset(context.assets, "fonts/nexa_bold.otf")
        this.typeface = typeface
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
    }


}