package com.chimpcode.discount

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import com.chimpcode.discount.adapters.PageAdapter
import com.chimpcode.discount.fragments.IFragmentInteractionListener
import com.chimpcode.discount.ui.views.MkDrawer
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.view.*


class HomeActivity : AppCompatActivity(), IFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val discountToolbar : Toolbar = findViewById(R.id.discount_toolbar)
        discountToolbar.toolbar_title.text = "Goint"
        setSupportActionBar(discountToolbar)
        MkDrawer.createOne(discountToolbar, this)

        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = PageAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)

    }


    override fun onFragmentInteraction(TAG: String, uri: Uri) {
    }

}
