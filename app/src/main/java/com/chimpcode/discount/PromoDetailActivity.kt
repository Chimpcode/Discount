package com.chimpcode.discount

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import com.bumptech.glide.Glide
import com.chimpcode.discount.adapters.PromoAdapter
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.models.Post
import com.chimpcode.discount.ui.views.MkDrawer.Companion.createOne
import kotlinx.android.synthetic.main.activity_promo_detail.*

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

        val horizontalLayoutM : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        additional_promos_recycler.layoutManager = horizontalLayoutM
        additional_promos_recycler.itemAnimator = DefaultItemAnimator()
        additional_promos_recycler.adapter = PromoAdapter(this, ArrayList<Post>(), PromoConstants.MINI)
        (additional_promos_recycler.adapter as PromoAdapter).fillSampleData()

        label_additional_promos.text = getString(R.string.view_more_promos_title, post.by)
    }

}
