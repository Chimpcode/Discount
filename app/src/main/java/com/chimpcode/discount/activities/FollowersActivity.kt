package com.chimpcode.discount.activities

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import com.chimpcode.discount.GointApplication
import com.chimpcode.discount.R
import com.chimpcode.discount.adapters.FollowerAdapter
import com.chimpcode.discount.adapters.FollowerInteractorListener
import com.chimpcode.discount.data.Company
import com.chimpcode.discount.ui.views.MkDrawer
import com.chimpcode.discount.viewmodels.FollowersViewModel
import com.facebook.Profile
import kotlinx.android.synthetic.main.activity_siguiendo.*
import kotlinx.android.synthetic.main.toolbar.view.*

class FollowersActivity : AppCompatActivity(), FollowerInteractorListener {

    private var mAdapter: FollowerAdapter? = null
    private var followersViewModel : FollowersViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siguiendo)

//        SHARED
        val sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE) ?: return
        val fullname = sharedPref.getString(getString(R.string.fullname), "***")
        val userId : String? = sharedPref.getString(getString(R.string.user_id), null)
        val email = sharedPref.getString(getString(R.string.email_user), "***@***.**")
        val discountToolbar : Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        discountToolbar.toolbar_title.text = "Siguiendo"
        setSupportActionBar(discountToolbar)
        MkDrawer().createOne(discountToolbar, this, Profile.getCurrentProfile().name, email)


        mAdapter = FollowerAdapter(this)
        val mLayoutManager : RecyclerView.LayoutManager= GridLayoutManager(this, 1)
        list_siguiendo.layoutManager = mLayoutManager
        list_siguiendo.adapter = mAdapter

        followersViewModel = ViewModelProviders.of(this)[FollowersViewModel::class.java]
        followersViewModel!!.apolloClient = (application as GointApplication).apolloClient()
        followersViewModel!!.userId = userId
        followersViewModel!!.companies().observe(this, object : Observer<List<Company>> {
            override fun onChanged(companies: List<Company>?) {
                if (companies != null) {
                    mAdapter!!.setData(companies)
                }
            }
        })
    }

    override fun onClickSubscribe(companyId: String, checked: Boolean) {
//        SUBSCRIBE
        val sp = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)
        val userId = sp.getString(getString(R.string.user_id), "")
        if (checked) {
            followersViewModel?.subscribeToCompany(companyId =  companyId, userId = userId)
        } else {
            followersViewModel?.unsubscribeFromCompany(companyId =  companyId, userId = userId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView : SearchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!TextUtils.isEmpty(query)) {
//                    mAdapter?.filterData(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter?.filterData(newText)
                return true
            }
        })
        return true
    }
}
