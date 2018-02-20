package com.chimpcode.discount.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.chimpcode.discount.GointApplication
import com.chimpcode.discount.R
import com.chimpcode.discount.adapters.IListener
import com.chimpcode.discount.adapters.PromoAdapter
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.data.Post
import com.chimpcode.discount.ui.views.MkDrawer
import com.chimpcode.discount.ui.views.showSnackbarWithMessage
import com.chimpcode.discount.viewmodels.MyPostsViewModel
import com.facebook.Profile
import kotlinx.android.synthetic.main.fragment_promo_list.*
import kotlinx.android.synthetic.main.fragment_promo_list.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class MisPromocionesActivity : AppCompatActivity(), IListener {

    var adapter : PromoAdapter? = null
    var myPostsViewModel : MyPostsViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_promociones)

//        TOOLBAR
        val discountToolbar : Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        discountToolbar.toolbar_title.text = "Mis promociones"
        setSupportActionBar(discountToolbar)

//        SHARED
        val sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE) ?: return
        val email = sharedPref.getString(getString(R.string.email_user), "***@***.**")
        val userId = sharedPref.getString(getString(R.string.user_id), "")

        MkDrawer().createOne(discountToolbar, this, Profile.getCurrentProfile().name, email)


        val app = application as GointApplication
        val mLayoutManager : RecyclerView.LayoutManager = GridLayoutManager(this, 1)
        adapter = PromoAdapter(this, PromoConstants.CARD, this)
        recycler!!.layoutManager = mLayoutManager
        recycler!!.itemAnimator = DefaultItemAnimator()
        recycler!!.adapter = adapter

//        VIEW MODEL
        myPostsViewModel = ViewModelProviders.of(this)[MyPostsViewModel::class.java]
        myPostsViewModel!!.userId = userId
        myPostsViewModel!!.apolloClient = app.apolloClient()!!
        myPostsViewModel!!.fetchMyPosts()
        myPostsViewModel!!.myposts().observe(this, object : Observer<List<Post>> {
            override fun onChanged(posts: List<Post>?) {
                if (posts != null) {
                    updateUI(posts)
                }
            }
        })
    }

    private fun updateUI(posts: List<Post>) {
//        UPDATE UI ADAPTER FOR LISTS POSTS
        adapter!!.setData(posts)
    }

    override fun onLikeOrDislikePostClick(postId: String) {

        val sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)
        val userId = sharedPref.getString(getString(R.string.user_id), "")
        myPostsViewModel!!.unlikePost(userId = userId, postId = postId)

        showSnackbarWithMessage(this, "Promocion borrada")
    }
}
