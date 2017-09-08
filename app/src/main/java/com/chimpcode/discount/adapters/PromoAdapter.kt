package com.chimpcode.discount.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chimpcode.discount.R

/**
 * Created by anargu on 9/2/17.
 */
class PromoAdapter( _context :Context, _promoList : Array<String>) : RecyclerView.Adapter<PromoAdapter.PromoCardViewHolder>() {

    private val context : Context = _context
    private val promoList : Array<String>

    init {
        promoList = _promoList
    }

    override fun onBindViewHolder(holder: PromoCardViewHolder?, position: Int) {

//        val result: String =  promoList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PromoCardViewHolder {
        val itemView : View = LayoutInflater.from(parent?.context).inflate(R.layout.card_promo_layout, parent, false)

        return PromoCardViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return promoList.size
    }


    class PromoCardViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            super.itemView

        }
    }
}

