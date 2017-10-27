package com.chimpcode.discount

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import com.chimpcode.discount.adapters.PromoAdapter
import com.chimpcode.discount.adapters.SiguiendoAdapter
import com.chimpcode.discount.models.Marca
import com.chimpcode.discount.utils.MkDrawer
import kotlinx.android.synthetic.main.activity_siguiendo.*
import kotlinx.android.synthetic.main.fragment_promo_list.view.*

class SiguiendoActivity : AppCompatActivity() {

    private var mAdapter: SiguiendoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siguiendo)


        var mAdapter = SiguiendoAdapter(this, fillData())

        val mLayoutManager : RecyclerView.LayoutManager= GridLayoutManager(this, 1)

        list_siguiendo.layoutManager = mLayoutManager

        list_siguiendo.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        val discountToolbar : Toolbar = findViewById<Toolbar>(R.id.discount_toolbar)
        setSupportActionBar(discountToolbar)
        MkDrawer.createOne(discountToolbar, this)
    }

    fun fillData () : ArrayList<Marca>{
        var marcaList = ArrayList<Marca>()

        marcaList.add(Marca("1", "Pizza Hut"))
        marcaList.add(Marca("1", "Dunkin Donuts"))
        marcaList.add(Marca("1", "Don Belisario"))
        marcaList.add(Marca("1", "Papa Johns"))
        marcaList.add(Marca("1", "KFC"))

        return marcaList
    }

}
