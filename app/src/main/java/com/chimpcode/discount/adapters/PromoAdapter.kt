package com.chimpcode.discount.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chimpcode.discount.PromoDetailActivity
import com.chimpcode.discount.R
import com.chimpcode.discount.common.GeoPromoConstants
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.common.PromoDelegateAdapter
import com.chimpcode.discount.models.Post
import com.chimpcode.discount.models.PromLocation
import kotlinx.android.synthetic.main.card_promo_layout.view.*

/**
 * Created by anargu on 9/2/17.
 */
class PromoAdapter(
        _context :Context, _items : ArrayList<Post>,
        private val viewType : Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val context : Context = _context
    private val items: ArrayList<Post>
    private var delegateAdapters = SparseArrayCompat<PromoDelegateAdapter>()

    init {
        delegateAdapters.put(PromoConstants.CARD, CardPromoAdapter(context))
        delegateAdapters.put(PromoConstants.GRID, GridPromoAdapter(context))
        delegateAdapters.put(PromoConstants.MINI, MiniPromoAdapter(context))
        items = _items
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

    fun fillSampleData() {

        for (i in 1..12) {
            items.add(Post(
                    i.toString(),"Burger King", "2 days ago", "Promo A Mega Combo",
                    "https://source.unsplash.com/random/600x600",
                    "Simple descripcion",
                    "Miraflores",
                    PromLocation(-11.891822f, -77.043371f),
                    3
            ))
        }

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
}

