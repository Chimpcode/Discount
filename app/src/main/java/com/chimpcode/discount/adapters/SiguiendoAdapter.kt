package com.chimpcode.discount.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chimpcode.discount.R
import com.chimpcode.discount.models.Marca
import com.chimpcode.discount.models.Post
import kotlinx.android.synthetic.main.card_promo_layout.view.*
import kotlinx.android.synthetic.main.item_seguidores.view.*

/**
 * Created by anargu on 9/25/17.
 */
class SiguiendoAdapter (_context :Context, _marcaList: ArrayList<Marca>) : RecyclerView.Adapter<SiguiendoAdapter.ItemViewHolder>(){

    private val context : Context = _context
    private val marcaList: ArrayList<Marca>

    init {
        marcaList = _marcaList
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        holder?.bindData(marcaList.get(position))
        holder?.getView()?.checkBox?.setOnClickListener {
            holder.getView().checkBox?.isChecked = !holder.getView().checkBox.isChecked
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        val itemView : View = LayoutInflater.from(parent?.context).inflate(R.layout.item_seguidores, parent, false)

        itemView.setOnClickListener {
        }

        return SiguiendoAdapter.ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return marcaList.size
    }


    class ItemViewHolder (itemView : View) :RecyclerView.ViewHolder(itemView){

        fun bindData(marca: Marca) {
            itemView.marca_name.text= marca.name
        }

        fun getView() : View{
            return itemView
        }
    }
}