package com.chimpcode.discount

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.bumptech.glide.Glide
import com.chimpcode.discount.models.Post
import com.chimpcode.discount.utils.MkDrawer.Companion.createOne
import kotlinx.android.synthetic.main.activity_promo_detail.*
import kotlinx.android.synthetic.main.toolbar.view.*

class PromoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo_detail)

        val discountToolbar: Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        setSupportActionBar(discountToolbar)
        createOne(discountToolbar, this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        discountToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val post : Post = intent.getSerializableExtra("post") as Post
        post_title.text = post.title
        Glide.with(this)
                .load(post.image)
                .into(post_image)
//        post_address.text = post.address
//        post_address_2.text = post.address
//        elapsed_time.text = post.created_at
//        post_description.text = post.description

    }

//
//    fun createDrawer(_toolbar: Toolbar) {
//
//        drawer {
//            toolbar = _toolbar
//            accountHeader {
//                background = R.color.primary_dark
//                profile("Samantha", "samantha@gmail.com") {
//                    icon = R.drawable.ic_person
//                }
//            }
//            primaryItem("Ofertas") { icon = R.drawable.ic_stars }
//            divider {}
//            primaryItem("Mis Promociones") { icon = R.drawable.ic_local_offer }
//            primaryItem("Seguidos") { icon = R.drawable.ic_star }
//            primaryItem("Ajustes") { icon = R.drawable.ic_build }
//            footer {
//                primaryItem("About Us") { icon = R.drawable.ic_info_outline }
//            }
//        }
//    }
}
