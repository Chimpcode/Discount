package com.chimpcode.discount.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.chimpcode.discount.R
import com.chimpcode.discount.common.extensions.inflate
import com.chimpcode.discount.models.MarkerData
import kotlinx.android.synthetic.main.dialog_geo_promo_item.view.*
import kotlinx.android.synthetic.main.marker_info_view.view.*

/**
 * Created by anargu on 11/16/17.
 */
class GeoPromoViewItem() : GeoPromoDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: GeoPromoViewType) {
        holder as TurnsViewHolder
        holder.bind(item as MarkerData)
    }

    class TurnsViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.dialog_geo_promo_item)) {

        fun bind(item : MarkerData) {
            itemView.promo_text.text = item.label
        }
    }
}