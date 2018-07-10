package com.chimpcode.discount.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.chimpcode.discount.common.*
import com.chimpcode.discount.data.GeoPost

/**
 * Created by anargu on 11/14/17.
 */
class GeoPromoDialogAdapter(private val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder >() {

    private val items : ArrayList<PromoViewType>
    private val geoPromoViewMoreAdapter = GeoPromoViewMoreAdapter(context)
    private var delegateAdapters = SparseArrayCompat<PromoDelegateAdapter>()
    val viewMoreItem = object : PromoViewType {
        override fun getViewType() = GeoPromoConstants.VIEW_MORE
    }

    init {
        delegateAdapters.put(GeoPromoConstants.VIEW_MORE, geoPromoViewMoreAdapter)
        delegateAdapters.put(GeoPromoConstants.ITEM, GeoPromoViewItemAdapter(context))
        items = ArrayList<PromoViewType>()
    }

    fun setData(promos : List<GeoPost>) {
        geoPromoViewMoreAdapter.companyId = promos[0].by.id

        items.clear()
        items.addAll(promos)
        items.add(viewMoreItem)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun getItemViewType(position: Int): Int = this.items[position].getViewType()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.items[position])
    }
}