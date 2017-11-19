package com.chimpcode.discount

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.chimpcode.discount.ui.views.MkDrawer
import kotlinx.android.synthetic.main.toolbar.view.*

class MisPromocionesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_promociones)

        val discountToolbar : Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        discountToolbar.toolbar_title.text = "Mis promociones"
        setSupportActionBar(discountToolbar)
        MkDrawer.createOne(discountToolbar, this)

    }


}
