package com.chimpcode.discount.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chimpcode.discount.R
import com.chimpcode.discount.common.PromoDelegateAdapter
import com.chimpcode.discount.common.PromoViewType
import com.chimpcode.discount.common.extensions.inflate
import com.chimpcode.discount.data.GeoPost
import kotlinx.android.synthetic.main.card_promo_layout.view.*
import kotlinx.android.synthetic.main.dialog_geo_promo_item.view.*

/**
 * Created by anargu on 11/16/17.
 */
class GeoPromoViewItemAdapter(ctx : Context) : PromoDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: PromoViewType) {
        holder as TurnsViewHolder
        holder.bind(item as GeoPost)
    }

    class TurnsViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.dialog_geo_promo_item)) {

        fun bind(item : GeoPost) {
            itemView.promo_text.text = item.title
            if (item.image != "") {
                Glide.with(itemView)
                        .load(item.image)
                        .into(itemView.image_view)
            }
        }
    }
}