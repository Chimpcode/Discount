package com.chimpcode.discount.ui.views

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chimpcode.discount.R
import com.chimpcode.discount.adapters.GeoPromoDialogAdapter
import com.chimpcode.discount.data.GeoPost
import kotlinx.android.synthetic.main.dialog_geo_promo_tienda.*

/**
 * Created by anargu on 11/15/17.
 */
class PromoGeoItemsDialog(private val ctx: Context,
                          private val promos : List<GeoPost>)
    : Dialog(ctx), View.OnClickListener {

    private val SPAN_COUNT = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_geo_promo_tienda)

        val mlayoutManager : RecyclerView.LayoutManager = GridLayoutManager(this@PromoGeoItemsDialog.context, SPAN_COUNT)
        list_promo.layoutManager = mlayoutManager
        list_promo.itemAnimator = DefaultItemAnimator()
        list_promo.addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))
        this.list_promo.adapter = GeoPromoDialogAdapter(context)
        (list_promo.adapter as GeoPromoDialogAdapter).setData(promos)
    }

    override fun onClick(v: View) {

    }

}