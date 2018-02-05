package com.chimpcode.discount.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.chimpcode.discount.R
import com.chimpcode.discount.ui.views.MkDrawer
import com.facebook.Profile
import kotlinx.android.synthetic.main.toolbar.view.*

class AboutGointActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_goint)


//        SHARED
        val sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE) ?: return
        val fullname = sharedPref.getString(getString(R.string.fullname), "***")
        val email = sharedPref.getString(getString(R.string.email_user), "***@***.**")

        val discountToolbar : Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        discountToolbar.toolbar_title.text = "Sobre Goint"
        setSupportActionBar(discountToolbar)
        MkDrawer().createOne(discountToolbar, this, Profile.getCurrentProfile().name, email)
    }
}
