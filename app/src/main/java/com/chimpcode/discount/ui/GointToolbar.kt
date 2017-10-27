package com.chimpcode.discount.ui

import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.widget.FrameLayout
import com.chimpcode.discount.R

/**
 * Created by anargu on 10/20/17.
 */
class GointToolbar : FrameLayout {

    private val ctx : Context

    constructor(_ctx : Context): super(_ctx) {
        ctx = _ctx
        init()
    }
    constructor(_ctx : Context, attrs : AttributeSet): super(_ctx, attrs) {
        ctx = _ctx
        init()
    }

    constructor(_ctx : Context, attrs : AttributeSet, defStyle : Int): super(_ctx, attrs, defStyle) {
        ctx = _ctx
        init()
    }

    private fun init() {
        inflate(ctx, R.layout.toolbar, this)
    }
}