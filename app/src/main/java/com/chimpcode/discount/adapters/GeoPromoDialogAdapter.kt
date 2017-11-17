package com.chimpcode.discount.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chimpcode.discount.R
import com.chimpcode.discount.common.*
import com.chimpcode.discount.models.MarkerData
import kotlinx.android.synthetic.main.dialog_geo_promo_item.view.*

/**
 * Created by anargu on 11/14/17.
 */
class GeoPromoDialogAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder >() {

    private val items : ArrayList<GeoPromoViewType>
    private var delegateAdapters = SparseArrayCompat<GeoPromoDelegateAdapter>()
    val viewMoreItem = object : GeoPromoViewType {
        override fun getViewType() = GeoPromoConstants.VIEW_MORE
    }

    init {
        delegateAdapters.put(GeoPromoConstants.VIEW_MORE, GeoPromoViewMore())
        delegateAdapters.put(GeoPromoConstants.ITEM, GeoPromoViewItem())
        items = ArrayList<GeoPromoViewType>()
    }

    fun setData(promos : List<MarkerData>) {
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
//        return if (viewType == GeoPromoConstants.ITEM) {
//            GeoPromoViewItem().onCreateViewHolder(parent)
//        }
//        else {
//            GeoPromoViewMore().onCreateViewHolder(parent)
//        }
    }

    override fun getItemViewType(position: Int): Int = this.items[position].getViewType()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.items[position])
//        return if (items[position].getViewType() == GeoPromoConstants.ITEM) {
//            GeoPromoViewItem().onBindViewHolder(holder, items[position])
//        }
//        else {
//            GeoPromoViewMore().onBindViewHolder(holder, items[position])
//        }
    }

}