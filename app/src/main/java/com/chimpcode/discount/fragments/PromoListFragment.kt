package com.chimpcode.discount.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chimpcode.discount.GointApplication
import com.chimpcode.discount.R
import com.chimpcode.discount.adapters.IListener
import com.chimpcode.discount.adapters.PromoAdapter
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.data.GointCategory
import com.chimpcode.discount.data.Post
import com.chimpcode.discount.ui.views.GointChip
import com.chimpcode.discount.ui.views.showSnackbarWithMessage
import com.chimpcode.discount.viewmodels.ListPostViewModel
import kotlinx.android.synthetic.main.fragment_promo_list.view.*

class PromoListFragment : Fragment(), IListener, SwipeRefreshLayout.OnRefreshListener {

    private var TAG = javaClass.toString()

    private var mListener: IFragmentInteractionListener? = null
    val app by lazy { activity!!.application as GointApplication }
    private var listPostViewModel: ListPostViewModel? = null
    var adapter : PromoAdapter? = null

    val categoryViewList = ArrayList<GointChip>()
    var swipeRefreshLayout : SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        listPostViewModel = ViewModelProviders.of(activity!!)[ListPostViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_promo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mLayoutManager : RecyclerView.LayoutManager = GridLayoutManager(context, 1)
        adapter = PromoAdapter(context!!, PromoConstants.CARD, this)

        swipeRefreshLayout = view.swipe_refresh_layout
        swipeRefreshLayout!!.setColorSchemeColors(
                ContextCompat.getColor(activity!!, R.color.primary),
                ContextCompat.getColor(activity!!, R.color.bellezaColor),
                ContextCompat.getColor(activity!!, R.color.entretenimientoColor),
                ContextCompat.getColor(activity!!, R.color.baresColor),
                ContextCompat.getColor(activity!!, R.color.restaurantsColor),
                ContextCompat.getColor(activity!!, R.color.serviciosColor),
                ContextCompat.getColor(activity!!, R.color.variosColor)
        )
        swipeRefreshLayout!!.setOnRefreshListener(this)

        view.recycler!!.layoutManager = mLayoutManager
        view.recycler!!.itemAnimator = DefaultItemAnimator()
        view.recycler!!.adapter = adapter
        createGointChips(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (listPostViewModel != null) {
            listPostViewModel!!.posts().observe(this, object : Observer<List<Post>> {
                override fun onChanged(posts: List<Post>?) {
                    if (posts != null) {
                        Log.d("$TAG ***", posts.size.toString())
                        adapter?.setData(posts)
                        swipeRefreshLayout?.isRefreshing = false
//                        likePost
                    }
                }
            })
            listPostViewModel!!.activeCategories().observe(this, object : Observer<List<GointCategory>> {
                override fun onChanged(activeCategories: List<GointCategory>?) {
                    if (activeCategories != null) {
                        updateUI(activeCategories)
                    }
                }
            })
            listPostViewModel!!.myposts().observe(this, object : Observer<List<Post>> {
                override fun onChanged(_myposts: List<Post>?) {
                    if (_myposts != null) {
                        adapter!!.setMyPosts(_myposts)
                    }
                }

            })
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement IFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun createGointChips(fragmentView: View) {

        val categories = listPostViewModel!!.allCategoriesDataView
        for (x in 0..(categories.size-1)) {
            val chip = GointChip(context!!)
            chip.text = categories[x].text
            chip.setColor(categories[x].colorId)
            chip.categoryData = categories[x]

            categoryViewList.add(chip)
            (fragmentView.chips_container as ViewGroup).addView(chip)

            chip.setOnClickListener {

                Log.d(TAG, "Click chip")
                if (listPostViewModel != null) {
                    swipeRefreshLayout?.isRefreshing = true
                    listPostViewModel!!.switchCategory(chip.categoryData!!.text)
                }
            }
        }
    }

    fun updateUI (activeCategories : List<GointCategory>) {
        if (activeCategories.isNotEmpty()) {
            for (gointChip: GointChip in categoryViewList) {
                if (activeCategories[0].text == gointChip.text) {
                    gointChip.turnOnChip()
                } else {
                    gointChip.turnOffChip()
                }
            }
            mListener!!.onChangeAppColor(ContextCompat.getColor(context!!,  activeCategories[0].colorId))
        }
    }

    override fun onLikeOrDislikePostClick(postId: String) {
        val sharedPref = context!!.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)
        val userId = sharedPref.getString(getString(R.string.user_id), "")
        listPostViewModel!!.likePost(userId = userId, postId = postId)

        showSnackbarWithMessage(activity!!, "Promocion guardado")
    }

    override fun onRefresh() {
//        swipeRefreshLayout?.isRefreshing = true
        listPostViewModel!!.fetchPosts()
    }

}
