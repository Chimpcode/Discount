package com.chimpcode.discount.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chimpcode.discount.PromoDetailActivity
import com.chimpcode.discount.R
import com.chimpcode.discount.models.Post
import kotlinx.android.synthetic.main.card_promo_layout.view.*

/**
 * Created by anargu on 9/2/17.
 */
class PromoAdapter( _context :Context, _postList : List<Post>) : RecyclerView.Adapter<PromoAdapter.PromoCardViewHolder>() {

    private val context : Context = _context
    private val postList: List<Post>

    init {
        postList = _postList
    }

    override fun onBindViewHolder(holder: PromoCardViewHolder?, position: Int) {

//        val result: String =  postList[position]
        if (holder != null) {
            holder.bindData(postList.get(position))
            holder.getView().setOnClickListener {

                val intent = Intent(context, PromoDetailActivity::class.java)
                intent.putExtra("post", postList.get(position))
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PromoCardViewHolder {
        val itemView : View = LayoutInflater.from(parent?.context).inflate(R.layout.card_promo_layout, parent, false)

        itemView.setOnClickListener {
        }

        return PromoCardViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postList.size
    }


    class PromoCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(post: Post) {
            itemView.promo_title.text = post.title
            Glide.with(itemView)
                    .load(post.image)
                    .into(itemView.image_post)
        }

        fun getView() : View {
            return itemView
        }

    }
}

