package com.chimpcode.discount

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.chimpcode.discount.adapters.PromoAdapter
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.models.Post
import com.chimpcode.discount.ui.views.MkDrawer
import com.chimpcode.discount.ui.views.utils.GridSpacingItemDecoration
import com.chimpcode.discount.ui.views.utils.dpToPx
import kotlinx.android.synthetic.main.activity_profile_store.*

class ProfileStoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_store)


        val discountToolbar: Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        setSupportActionBar(discountToolbar)
        MkDrawer.createOne(discountToolbar, this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        discountToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val mLayoutManager : RecyclerView.LayoutManager= GridLayoutManager(this, 2)
        grid_promo_mini.layoutManager = mLayoutManager
        grid_promo_mini.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(this, 10f), true))
        grid_promo_mini.itemAnimator= DefaultItemAnimator()

        grid_promo_mini.adapter = PromoAdapter(this, ArrayList<Post>(), PromoConstants.GRID)
        (grid_promo_mini.adapter as PromoAdapter).fillSampleData()
    }
}
