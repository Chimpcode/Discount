package com.chimpcode.discount.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import com.bumptech.glide.Glide
import com.chimpcode.discount.GointApplication
import com.chimpcode.discount.R
import com.chimpcode.discount.adapters.IListener
import com.chimpcode.discount.adapters.PromoAdapter
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.data.Post
import com.chimpcode.discount.ui.views.MkDrawer
import com.chimpcode.discount.viewmodels.PostViewModel
import com.facebook.Profile
import kotlinx.android.synthetic.main.activity_promo_detail.*

class PromoDetailActivity : AppCompatActivity(), IListener {

    var postViewModel: PostViewModel? = null
    val TAG = "PromoDetailActivity :: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo_detail)

//        TOOLBAR
        val sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE) ?: return
        val fullname = sharedPref.getString(getString(R.string.fullname), "***")
        val email = sharedPref.getString(getString(R.string.email_user), "***@***.**")
        val discountToolbar: Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        setSupportActionBar(discountToolbar)
        MkDrawer().createOne(discountToolbar, this, Profile.getCurrentProfile().name, email)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        discountToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val horizontalLayoutM : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        additional_promos_recycler.layoutManager = horizontalLayoutM
        additional_promos_recycler.itemAnimator = DefaultItemAnimator()
        additional_promos_recycler.adapter = PromoAdapter(this, PromoConstants.MINI, this)
        (additional_promos_recycler.adapter as PromoAdapter)

        val app = application as GointApplication
        val bundle = intent.extras
        val post : Post =  bundle.getSerializable("post") as Post

        postViewModel = ViewModelProviders.of(this)[PostViewModel::class.java]
        postViewModel!!.settingApolloClient(app.apolloClient()!!)
        postViewModel!!.postId(post.id)
        postViewModel!!.fetchSinglePost()
        postViewModel!!.post().observe(this, object : Observer<Post> {
            override fun onChanged(post: Post?) {
                if (post != null) {
                    updateUI(post)
                }
            }
        })

        share_button.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_SEND)

//            # change the type of data you need to share,
//            # for image use "image/*"
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            startActivity(Intent.createChooser(intent, "Share"))
        }
        goToMapView.setOnClickListener {
            Log.d(TAG, "GoToMapView")
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("promo_id", "aaaaaaa")
            startActivity(intent)
        }

    }

    private fun updateUI(post: Post) {
        post_title.text = post.title
        post_description.text = post.description
        if (post.image != "") {
            Glide.with(this)
                    .load(post.image)
                    .into(post_image)
        }
        label_additional_promos.text = getString(R.string.view_more_promos_title, post.by.commercialName)

        if (additional_promos_recycler.adapter != null) {
            (additional_promos_recycler.adapter as PromoAdapter).setData(post.by.postsAssigned)
        } else {
            Log.d("ProfileStoreActivity***", "not adapter")
        }
    }

    override fun onLikeOrDislikePostClick(postId: String) {
    }
}
