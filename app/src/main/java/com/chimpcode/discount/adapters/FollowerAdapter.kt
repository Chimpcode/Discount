package com.chimpcode.discount.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.bumptech.glide.Glide
import com.chimpcode.discount.R
import com.chimpcode.discount.data.Company
import kotlinx.android.synthetic.main.item_seguidores.view.*
import me.rishabhkhanna.customtogglebutton.CustomToggleButton

/**
 * Created by anargu on 9/25/17.
 */
class FollowerAdapter(val interactor : FollowerInteractorListener) : RecyclerView.Adapter<FollowerAdapter.ItemViewHolder>(){

    private val companyList: ArrayList<Company> = ArrayList<Company>()
    private val companyListOriginal: ArrayList<Company> = ArrayList<Company>()


    fun setData(_marcaList: List<Company>) {
        companyList.clear()
        companyList.addAll(_marcaList)
        companyListOriginal.clear()
        companyListOriginal.addAll(_marcaList)
        notifyDataSetChanged()
    }

    fun filterData(searchText : String) {
        if (searchText == "") {
            companyList.clear()
            companyList.addAll(companyListOriginal)
            notifyDataSetChanged()
        } else {
            val foundList = ArrayList<Company>()
            companyList.filterTo(foundList) { it.commercialName.toLowerCase().contains(searchText.toLowerCase()) }
            companyList.clear()
            companyList.addAll(foundList)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        holder?.bindData(companyList[position])
        holder?.itemView!!.subscribed_checkbox.setOnClickListener {
            interactor.onClickSubscribe(companyList[position].id, (it as CustomToggleButton).isChecked)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        val itemView : View = LayoutInflater.from(parent?.context).inflate(R.layout.item_seguidores, parent, false)

        itemView.subscribed_checkbox.setOnClickListener {
            Log.d("CHECKBOX ***",  "cheking ... ${itemView.subscribed_checkbox.isChecked}")
        }

        return FollowerAdapter.ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return companyList.size
    }




    class ItemViewHolder (itemView : View) :RecyclerView.ViewHolder(itemView){

        fun bindData(company: Company) {
            itemView.marca_name.text = company.commercialName
            if (company.isSubscribed != null) {
                itemView.subscribed_checkbox.isChecked = company.isSubscribed!!
            }
            Glide.with(itemView)
                    .load(company.logoUrl)
                    .into(itemView.company_image)
        }

        fun getView() : View{
            return itemView
        }
    }
}

interface FollowerInteractorListener {
    fun onClickSubscribe(companyId: String, checked: Boolean)
}