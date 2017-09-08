package com.chimpcode.discount

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import com.chimpcode.discount.adapters.PageAdapter
import com.chimpcode.discount.fragments.IFragmentInteractionListener
import com.chimpcode.discount.fragments.PromoListFragment
import com.chimpcode.discount.fragments.PromoMapFragment
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), IFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val discountToolbar : Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        setSupportActionBar(discountToolbar)
        createDrawer(discountToolbar)

        tabLayout!!.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = PageAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)
    }

    fun createDrawer(_toolbar : Toolbar) {

        drawer {
            toolbar =  _toolbar
            accountHeader {
                background = R.color.primary_dark
                profile("Samantha", "samantha@gmail.com") {
                    icon = R.drawable.ic_person
                }
            }
            primaryItem("Ofertas") { icon = R.drawable.ic_stars}
            divider {}
            primaryItem("Cupones") { icon = R.drawable.ic_local_offer }
            primaryItem("Favoritos") { icon = R.drawable.ic_favorite  }
            primaryItem("Seguidos") { icon = R.drawable.ic_star }
            primaryItem("Ajustes") {icon = R.drawable.ic_build}
            footer{
                primaryItem("About Us") { icon = R.drawable.ic_info_outline }
            }
        }
    }

    override fun onFragmentInteraction(TAG: String, uri: Uri) {
    }

}
