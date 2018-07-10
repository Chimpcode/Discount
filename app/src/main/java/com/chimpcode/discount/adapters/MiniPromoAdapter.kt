package com.chimpcode.discount.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.chimpcode.discount.R
import com.chimpcode.discount.activities.PromoDetailActivity
import com.chimpcode.discount.common.PromoDelegateAdapter
import com.chimpcode.discount.common.PromoViewType
import com.chimpcode.discount.common.extensions.inflate
import com.chimpcode.discount.data.Post
import kotlinx.android.synthetic.main.additional_promos_item.view.*
import java.io.IOException
import android.app.Activity
import com.bumptech.glide.Glide


/**
 * Created by anargu on 11/19/17.
 */
class MiniPromoAdapter(private val ctx : Context) : PromoDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: PromoViewType) {
        holder as TurnsViewHolder
        holder.bind(item as Post)
        holder.itemView.setOnClickListener {
            val intent= Intent(ctx, PromoDetailActivity::class.java)
            val bundle = Bundle()
            try {
                bundle.putSerializable("post", item)
                intent.putExtras(bundle)
                ctx.startActivity(intent)
                (ctx as Activity).finish()
            } catch (e : IOException) {
                e.printStackTrace()
            }
        }
    }

    class TurnsViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.additional_promos_item)) {

        fun bind(item : Post) {
            itemView.promo_title.text = item.title
            Glide.with(itemView)
                    .load(item.image)
                    .into(itemView.mini_image_post)
        }
    }
}