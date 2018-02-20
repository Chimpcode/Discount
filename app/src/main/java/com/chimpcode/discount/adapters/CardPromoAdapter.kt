package com.chimpcode.discount.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.chimpcode.discount.activities.PromoDetailActivity
import com.chimpcode.discount.R
import com.chimpcode.discount.common.PromoDelegateAdapter
import com.chimpcode.discount.common.PromoViewType
import com.chimpcode.discount.common.extensions.inflate
import com.chimpcode.discount.data.Post
import kotlinx.android.synthetic.main.card_promo_layout.view.*
import java.io.IOException

/**
 * Created by anargu on 11/19/17.
 */
class CardPromoAdapter(private val ctx: Context,val listener: IListener, val idPosts : List<String>) : PromoDelegateAdapter {



    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = TurnsViewHolder(parent, idPosts, ctx)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: PromoViewType) {
        holder as TurnsViewHolder
        holder.bind(item as Post)
        holder.itemView.likeButton.setOnClickListener {
            (it as ImageButton).setColorFilter(ContextCompat.getColor(ctx, R.color.primary))
            listener.onLikeOrDislikePostClick(item.id)
        }
        holder.itemView.setOnClickListener {
            val intent= Intent(ctx, PromoDetailActivity::class.java)
            val bundle = Bundle()
            try {
                bundle.putSerializable("post", item)
                intent.putExtras(bundle)
                ctx.startActivity(intent)
            } catch (e : IOException) {
                e.printStackTrace()
            }
        }
    }

    class TurnsViewHolder(parent: ViewGroup, val idPosts: List<String>, val ctx: Context) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.card_promo_layout)
    ) {
        fun bind(item : Post) {
            itemView.promo_title.text = item.title
            if (item.distance != null) {
                itemView.distance.text = "${ "%.2f".format(item.distance!! / 1000) } km"
            }
            if (item.image != "") {
                Glide.with(itemView)
                        .load(item.image)
                        .into(itemView.image_post)
            }

            if (checkIsLiked(idPosts = idPosts, postId = item.id)) {
//                change color
                itemView.likeButton.setColorFilter(ContextCompat.getColor(ctx, R.color.primary))
            } else {
                itemView.likeButton.setColorFilter(ContextCompat.getColor(ctx, R.color.disabled))
            }
        }

        private fun checkIsLiked(idPosts: List<String>, postId : String) : Boolean {
            idPosts.firstOrNull{ it == postId } ?: return false
            return true
        }
    }

}