package com.chimpcode.discount.common

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.chimpcode.discount.R
import com.chimpcode.discount.common.extensions.inflate

/**
 * Created by anargu on 11/16/17.
 */
class GeoPromoViewMore () : GeoPromoDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: GeoPromoViewType) {
    }

    class TurnsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder (
            parent.inflate(R.layout.dialog_geo_viewmore_item)
    )

}