package com.chimpcode.discount.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.chimpcode.discount.R
import com.chimpcode.discount.common.PromoDelegateAdapter
import com.chimpcode.discount.common.PromoViewType
import com.chimpcode.discount.common.extensions.inflate
import com.chimpcode.discount.models.Post
import kotlinx.android.synthetic.main.additional_promos_item.view.*

/**
 * Created by anargu on 11/19/17.
 */
class MiniPromoAdapter(ctx : Context) : PromoDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: PromoViewType) {
        holder as TurnsViewHolder
        holder.bind(item as Post)
    }

    class TurnsViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.additional_promos_item)) {

        fun bind(item : Post) {
            itemView.promo_title.text = item.title
        }
    }
}