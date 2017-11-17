package com.chimpcode.discount.common

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by anargu on 11/16/17.
 */
interface GeoPromoDelegateAdapter {

    fun onCreateViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder

    fun onBindViewHolder(holder : RecyclerView.ViewHolder, item: GeoPromoViewType)
}