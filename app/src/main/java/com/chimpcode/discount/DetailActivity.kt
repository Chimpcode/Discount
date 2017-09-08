package com.chimpcode.discount

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.profile.profile

class PromoDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo_detail)


        val discountToolbar : Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        setSupportActionBar(discountToolbar)
        createDrawer(discountToolbar)
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
}
