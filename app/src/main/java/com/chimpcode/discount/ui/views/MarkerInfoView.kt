package com.chimpcode.discount.ui.views

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.chimpcode.discount.R
import com.chimpcode.discount.models.MarkerData
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.marker_info_view.view.*
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

/**
 * Created by anargu on 11/13/17.
 */
class MarkerInfoView(act : Activity, private val allMarkersMap: MutableMap<Marker, MarkerData>) : InfoWindowAdapter, OnInfoWindowClickListener {

    private val mWindow : View = act.layoutInflater.inflate(R.layout.marker_info_view, null)
    private val mContents : View = act.layoutInflater.inflate(R.layout.marker_info_view_contents, null)

    override fun getInfoContents(marker: Marker): View {
        render(marker, mContents)
        return mContents
    }

    override fun getInfoWindow(marker: Marker): View {
        render(marker, mWindow)
        return mWindow
    }

    private fun render(marker : Marker, view : View) {
        val data = allMarkersMap[marker]
        view.tv_title.text = marker.title
        view.tv_label.text = data!!.status
    }

    override fun onInfoWindowClick(marker: Marker) {
        Log.d(MarkerInfoView::class.java.simpleName, "Click! on WINDOW")
    }

}