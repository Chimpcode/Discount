package com.chimpcode.discount

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.chimpcode.discount.adapters.PageAdapter
import com.chimpcode.discount.fragments.IFragmentInteractionListener
import com.chimpcode.discount.utils.MkDrawer
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import kotlinx.android.synthetic.main.activity_home.*




class HomeActivity : AppCompatActivity(), IFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val discountToolbar : Toolbar = findViewById(R.id.discount_toolbar)
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
