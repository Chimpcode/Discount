package com.chimpcode.discount.activities

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import com.chimpcode.discount.GointApplication
import com.chimpcode.discount.R
import com.chimpcode.discount.adapters.PageAdapter
import com.chimpcode.discount.fragments.IFragmentInteractionListener
import com.chimpcode.discount.ui.views.MkDrawer
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.view.*
import android.view.WindowManager
import android.os.Build
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import com.chimpcode.discount.fragments.PromoListFragment
import com.chimpcode.discount.viewmodels.ListPostViewModel

class HomeActivity : AppCompatActivity(), IFragmentInteractionListener{

    var app : GointApplication? = null
    var gointToolbar : Toolbar? = null
    var tabView: TabLayout? = null
    val TAG = "HOME ACTIVITY"

    var listPostViewModel: ListPostViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        settingRangeSearchPosts()

//        TOOLBAR
        gointToolbar = findViewById(R.id.discount_toolbar)
        gointToolbar!!.toolbar_title.text = "Goint"
        setSupportActionBar(gointToolbar)

        val sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE) ?: return
        val fullname = sharedPref.getString(getString(R.string.fullname), "***")
        val email = sharedPref.getString(getString(R.string.email_user), "***@***.**")

        MkDrawer().createOne(gointToolbar!!, this, fullname, email)

//        TABS AND TAB LAYUOT
        tabView = tabLayout
        tabView!!.addTab(tabView!!.newTab())
        tabView!!.addTab(tabView!!.newTab())
        tabView!!.tabGravity = TabLayout.GRAVITY_FILL

//        VIEW PAGE ADAPTER
        val adapter = PageAdapter(supportFragmentManager, tabView!!.tabCount)
        viewPager.adapter = adapter

        tabView!!.setupWithViewPager(viewPager)

        val sp = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)
        val userId = sp.getString(getString(R.string.user_id), "")

//        VIEW MODEL
        val app = application
        listPostViewModel = ViewModelProviders.of(this)[ListPostViewModel::class.java]
        listPostViewModel!!.settingApolloClient((app as GointApplication).apolloClient!!)

        listPostViewModel!!.fetchPosts()
        listPostViewModel!!.fetchMyPosts(userId)

        listPostViewModel!!.setRateLatLng(getRangeSearchPosts())
    }

    fun settingRangeSearchPosts () {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val rangeSearchLocation = sharedPref.getFloat(getString(R.string.search_near_posts_by_location_range), 0f)

        if (rangeSearchLocation == 0f) {
            with(sharedPref.edit()){
                putFloat(getString(R.string.search_near_posts_by_location_range), 0.004f)
                commit()
            }
        }
    }

    fun getRangeSearchPosts() : Int{

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val range_search = sharedPref.getInt("slider_picker", 0)

        return range_search
    }

    override fun onFragmentInteraction(TAG: String, uri: Uri) {
    }

    private fun changeStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    override fun onChangeAppColor(color: Int) {

        val colorFrom = (gointToolbar!!.background as ColorDrawable).color
        val colorTo = ColorDrawable(color).color

        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)

        colorAnimation.addUpdateListener {
            animator ->
            val colorNumber : Int = animator.animatedValue as Int
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(colorNumber))
            tabView!!.background = ColorDrawable(colorNumber)
            changeStatusBarColor(colorNumber)
        }
        colorAnimation.setDuration(250)
        colorAnimation.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView : SearchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!TextUtils.isEmpty(query)) {
                    if (listPostViewModel != null) {
                        listPostViewModel!!.filterPostByText(query)
                        Log.d(TAG, "searching with text...")
                    }
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if (!TextUtils.isEmpty(newText)) {
//                    if (listPostViewModel != null) {
//                        listPostViewModel!!.filterPostByText(newText)
//                    }
                }
                return true
            }
        })
        return true
    }

}
