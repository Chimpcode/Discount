package com.chimpcode.discount.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.chimpcode.discount.GointApplication
import com.chimpcode.discount.R
import com.chimpcode.discount.adapters.IListener
import com.chimpcode.discount.adapters.PromoAdapter
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.data.Company
import com.chimpcode.discount.ui.views.MkDrawer
import com.chimpcode.discount.ui.views.utils.GridSpacingItemDecoration
import com.chimpcode.discount.ui.views.utils.dpToPx
import com.chimpcode.discount.viewmodels.StoreViewModel
import com.facebook.Profile
import kotlinx.android.synthetic.main.activity_profile_store.*

class ProfileStoreActivity : AppCompatActivity(), IListener {

    var storeViewModel : StoreViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_store)

//        DRAWER PROFILE DATA
        val discountToolbar: Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        setSupportActionBar(discountToolbar)
        //        SHARED
        val sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE) ?: return
        val fullname = sharedPref.getString(getString(R.string.fullname), "***")
        val email = sharedPref.getString(getString(R.string.email_user), "***@***.**")

        MkDrawer().createOne(discountToolbar, this, Profile.getCurrentProfile().name, email)

//        TOOLBAR
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        discountToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

//        GRID
        val mLayoutManager : RecyclerView.LayoutManager= GridLayoutManager(this, 2)
        grid_promo_mini.layoutManager = mLayoutManager
        grid_promo_mini.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(this, 10f), true))
        grid_promo_mini.itemAnimator= DefaultItemAnimator()

        grid_promo_mini.adapter = PromoAdapter(this, PromoConstants.GRID, this)

//        VIEW MODEL DATA BINDING TO UI
        val id = intent.getStringExtra("company_id")
        val app = application as GointApplication
        storeViewModel = ViewModelProviders.of(this)[StoreViewModel::class.java]
        storeViewModel!!.settingApolloClient(app.apolloClient()!!)
        storeViewModel!!.companyId(id) // automatic fetch company data
        storeViewModel!!.fetchCompanyById()

        storeViewModel!!.store().observe(this, object : Observer<Company> {
            override fun onChanged(company: Company?) {
                if (company != null) {
                    updateUI(company)
                }
            }
        })
    }

    fun updateUI(company: Company) {
        title_1.text = company.commercialName
        if (company.email != "") {
            title_2.text = company.email
        } else {
            title_2.text = "goint@example.com"
        }
        company_info.setOnClickListener {
            showTermsAndConditionsDialog(company.termsConditions?:"")
        }

        (grid_promo_mini.adapter as PromoAdapter).setData(company.postsAssigned)
    }

    fun showTermsAndConditionsDialog(text : String) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.terms_and_conditions_title_by_company))
        builder.setMessage(text)

        // add a button
        builder.setPositiveButton("OK", null)

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun onLikeOrDislikePostClick(postId: String) {
    }
}
