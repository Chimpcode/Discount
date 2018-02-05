package com.chimpcode.discount.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.chimpcode.discount.activities.ProfileStoreActivity
import com.chimpcode.discount.R
import com.chimpcode.discount.common.PromoDelegateAdapter
import com.chimpcode.discount.common.PromoViewType
import com.chimpcode.discount.common.extensions.inflate
import kotlinx.android.synthetic.main.dialog_geo_viewmore_item.view.*

/**
 * Created by anargu on 11/16/17.
 */
class GeoPromoViewMoreAdapter(private val ctx : Context) : PromoDelegateAdapter {

    var companyId = ""

    override fun onCreateViewHolder(parent: ViewGroup) = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: PromoViewType) {
        holder.itemView.view_more.setOnClickListener {
            val intent = Intent(ctx, ProfileStoreActivity::class.java)
            intent.putExtra("company_id", companyId)
            ctx.startActivity(intent)
        }
    }

    class TurnsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder (
            parent.inflate(R.layout.dialog_geo_viewmore_item)
    )

}