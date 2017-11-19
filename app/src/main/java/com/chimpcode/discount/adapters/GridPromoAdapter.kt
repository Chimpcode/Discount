package com.chimpcode.discount.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.chimpcode.discount.PromoDetailActivity
import com.chimpcode.discount.R
import com.chimpcode.discount.common.PromoDelegateAdapter
import com.chimpcode.discount.common.PromoViewType
import com.chimpcode.discount.common.extensions.inflate
import com.chimpcode.discount.models.Post
import kotlinx.android.synthetic.main.additional_promos_item.view.*

/**
 * Created by anargu on 11/19/17.
 */
class GridPromoAdapter(private val ctx : Context) : PromoDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: PromoViewType) {
        holder as TurnsViewHolder
        holder.bind(item as Post)
        holder.itemView.setOnClickListener {
            val intent= Intent(ctx, PromoDetailActivity::class.java)
            intent.putExtra("post", item)
            ctx.startActivity(intent)
        }
    }

    class TurnsViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.mini_card_promo_layout)) {

        fun bind(item : Post) {
            itemView.promo_title.text = item.title
        }
    }
}