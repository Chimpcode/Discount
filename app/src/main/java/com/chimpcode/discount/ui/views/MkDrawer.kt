package com.chimpcode.discount.ui.views

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import com.chimpcode.discount.*
import com.chimpcode.discount.activities.*
import com.facebook.login.LoginManager
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.act

/**
 * Created by anargu on 10/19/17.
 */
class MkDrawer () {


    var headerAccount : AccountHeader? =null
    var drawer : Drawer? = null
    var fullName : String = ""
    var email : String = ""

    fun createOne(_toolbar: Toolbar? = null, act: Activity, name: String, email: String): Drawer {

        val OFERTAS_POSITION = 1
        val MIS_PROMOCIONES_POSITION = 3
        val SIGUIENDO_POSITION = 4
        val AJUSTES_POSITION = 5
        val ABOUT_GOINT = 6
        val CERRAR_SESSION = 7

        val ofertas : PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(1).withName("Ofertas")
        val mis_promociones : SecondaryDrawerItem = SecondaryDrawerItem().withIdentifier(2).withName("Mis Promociones")
        val siguiendo: SecondaryDrawerItem = SecondaryDrawerItem().withIdentifier(3).withName("Siguiendo")
        val ajustes: SecondaryDrawerItem = SecondaryDrawerItem().withIdentifier(4).withName("Ajustes")
        val about_goint: SecondaryDrawerItem = SecondaryDrawerItem().withIdentifier(5).withName("Sobre Nosotros")
        val logOut: SecondaryDrawerItem = SecondaryDrawerItem().withIdentifier(6).withName("Cerrar Session")

        headerAccount = AccountHeaderBuilder()
                .withActivity(act)
                .withHeaderBackground(R.drawable.header_goint)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        ProfileDrawerItem().withName(name)
                                .withEmail(email)
                )
                .withOnAccountHeaderListener({ _, _, _ ->
                    false
                })
                .build()

        var drawerWithoutToolbar = DrawerBuilder()
                .withActivity(act)
                .withAccountHeader(headerAccount!!)
                .addDrawerItems(
                        ofertas,
                        DividerDrawerItem(),
                        mis_promociones,
                        siguiendo,
                        ajustes,
                        about_goint,
                        logOut
                )
                .withOnDrawerItemClickListener({ _ : View?, position: Int, itemDrawer : IDrawerItem<*, *>? ->
                    println("" + position  + " << click item drawer")
                    println("" + itemDrawer?.tag.toString() + " << click itemDrawer")
                    when (position) {
                        OFERTAS_POSITION -> {
                            if (act.javaClass.simpleName != HomeActivity::class.java.simpleName) {
                                val intent = Intent(act, HomeActivity::class.java)
                                act.startActivity(intent)
                                act.finish()
                            }
                        }
                        MIS_PROMOCIONES_POSITION -> {
                            if (act.javaClass.simpleName != MisPromocionesActivity::class.java.simpleName) {
                                val intent = Intent(act, MisPromocionesActivity::class.java)
                                act.startActivity(intent)
                            }
                        }
                        SIGUIENDO_POSITION -> {
                            if (act.javaClass.simpleName != FollowersActivity::class.java.simpleName) {
                                val intent = Intent(act, FollowersActivity::class.java)
                                act.startActivity(intent)
                            }
                        }
                        AJUSTES_POSITION -> {
                            if (act.javaClass.simpleName != SettingsActivity::class.java.simpleName) {
                                val intent = Intent(act, SettingsActivity::class.java)
                                act.startActivity(intent)
                            }
                        }
                        CERRAR_SESSION -> {
                            if (act.javaClass.simpleName != LoginActivity::class.java.simpleName) {
                                val intent = Intent(act, LoginActivity::class.java)
                                act.startActivity(intent)
                                LoginManager.getInstance().logOut()
                            }
                        }
                        ABOUT_GOINT -> {
                            if (act.javaClass.simpleName != AboutGointActivity::class.java.simpleName) {
                                val intent = Intent(act, AboutGointActivity::class.java)
                                act.startActivity(intent)
                            }
                        }
                    }
                    true
                })
        var drawerBuilder = drawerWithoutToolbar
        if (_toolbar != null) {
            drawerBuilder.withToolbar(_toolbar)
        }
        drawer = drawerBuilder.build()


        when(act.localClassName) {
            "ProfileStoreActivity",
            "PromoDetailActivity" -> {
                _toolbar?.alpha = 0.8f
                _toolbar?.toolbar_title?.visibility = View.INVISIBLE
                _toolbar?.background = ContextCompat.getDrawable(act, R.drawable.back_toolbar)

                drawer!!.actionBarDrawerToggle.isDrawerIndicatorEnabled = false
            }
        }
        if (act.localClassName == "PromoDetailActivity") {
        }

        this.drawer = drawer
        this.fullName = name
        this.email = email
        return drawer!!
    }


    fun changeHeader(drawer : Drawer, act : Activity, color : Int) {
        val drawable = ColorDrawable(color)

        val header = AccountHeaderBuilder()
                .withActivity(act)
                .withHeaderBackground(drawable)
                .addProfiles(
                        ProfileDrawerItem()
                                .withName(this.fullName)
                                .withEmail(this.email)
                                .withIcon(R.drawable.goint_avatar)
                )
                .build()
        drawer.removeHeader()
        drawer.setHeader(header.view, false)
    }

}