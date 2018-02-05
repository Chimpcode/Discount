package com.chimpcode.discount.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.common.PromoDelegateAdapter
import com.chimpcode.discount.data.Post

/**
 * Created by anargu on 9/2/17.
 */
class PromoAdapter(
        _context :Context,
        private val viewType : Int,
        listener : IListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val context : Context = _context
    private val items: ArrayList<Post> = ArrayList()
    private var delegateAdapters = SparseArrayCompat<PromoDelegateAdapter>()
    private val myPosts : ArrayList<Post> = ArrayList<Post>()

    init {
        val postIds = myPosts.map {
            it.id
        }
        delegateAdapters.put(PromoConstants.CARD, CardPromoAdapter(context, listener, postIds)) // card full width
        delegateAdapters.put(PromoConstants.GRID, GridPromoAdapter(context)) // grid promo
        delegateAdapters.put(PromoConstants.MINI, MiniPromoAdapter(context)) // mini promo
    }

    fun setMyPosts(myposts : List<Post>) {
        myPosts.clear()
        myPosts.addAll(myposts)
        notifyDataSetChanged()
    }

    fun setData(_items : List<Post>) {
        items.clear()
        items.addAll(_items)
        notifyDataSetChanged()
    }

    fun insertSingleData(item : Post) {
        items.add(item)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters[viewType].onBindViewHolder(holder, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters[viewType].onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

//    fun fetchData(apolloClient: ApolloClient?, activeCategories: ArrayList<GointCategory>) {
//        items.clear()
//        items.addAll(fetchPosts(apolloClient, activeCategories))
//    }
}


interface IListener {
    fun onLikeOrDislikePostClick(postId : String)
}

