package com.chimpcode.discount.utils

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import com.chimpcode.discount.*
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

/**
 * Created by anargu on 10/19/17.
 */
class MkDrawer () {

    companion object {
        fun createOne(_toolbar: Toolbar, act : Activity) {

            val OFERTAS_POSITION = 1
            val MIS_PROMOCIONES_POSITION = 3
            val SIGUIENDO_POSITION = 4
            val AJUSTES_POSITION = 5

            val ofertas : PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(1).withName("Ofertas")
            val mis_promociones : SecondaryDrawerItem = SecondaryDrawerItem().withIdentifier(2).withName("Mis Promociones")
            val siguiendo: SecondaryDrawerItem = SecondaryDrawerItem().withIdentifier(3).withName("Siguiendo")
            val ajustes: SecondaryDrawerItem = SecondaryDrawerItem().withIdentifier(4).withName("Ajustes")

            val headerDrawer : AccountHeader = AccountHeaderBuilder()
                    .withActivity(act)
                    .withHeaderBackground(R.drawable.header)
                    .addProfiles(
                            ProfileDrawerItem().withName("Mike Penz")
                                    .withEmail("aaa@aaa.com")
                                    .withIcon(ContextCompat.getDrawable(act, R.drawable.ic_person))
                    )
                    .withOnAccountHeaderListener({ _, _, _ ->
                        false
                    })
                    .build()

            DrawerBuilder()
                    .withActivity(act)
                    .withToolbar(_toolbar)
                    .withAccountHeader(headerDrawer)
                    .addDrawerItems(
                            ofertas,
                            DividerDrawerItem(),
                            mis_promociones,
                            siguiendo,
                            ajustes
                    )
                    .withOnDrawerItemClickListener({ _ : View?, position: Int, itemDrawer : IDrawerItem<*, *>? ->
                        println("" + position  + " << click item drawer")
                        println("" + itemDrawer?.tag.toString() + " << click itemDrawer")
                        when (position) {
                            OFERTAS_POSITION -> {
                                if (act.javaClass.simpleName != HomeActivity::class.java.simpleName) {
                                    val intent = Intent(act, HomeActivity::class.java)
                                    act.startActivity(intent)
                                }
                            }
                            MIS_PROMOCIONES_POSITION -> {
                                if (act.javaClass.simpleName != MisPromocionesActivity::class.java.simpleName) {
                                    val intent = Intent(act, MisPromocionesActivity::class.java)
                                    act.startActivity(intent)
                                }
                            }
                            SIGUIENDO_POSITION -> {
                                if (act.javaClass.simpleName != SiguiendoActivity::class.java.simpleName) {
                                    val intent = Intent(act, SiguiendoActivity::class.java)
                                    act.startActivity(intent)
                                }
                            }
                            AJUSTES_POSITION -> {
                                if (act.javaClass.simpleName != SettingsActivity::class.java.simpleName) {
                                    val intent = Intent(act, SettingsActivity::class.java)
                                    act.startActivity(intent)
                                }
                            }
                        }
                        true
                    })
                    .build()
        }
    }
}