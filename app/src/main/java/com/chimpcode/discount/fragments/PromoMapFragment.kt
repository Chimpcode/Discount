package com.chimpcode.discount.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chimpcode.discount.R
import com.google.android.gms.maps.*
import android.location.Location
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.Window
import com.chimpcode.discount.ui.views.PromoGeoItemsDialog
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_promo_map.view.*
import com.chimpcode.discount.data.*
import com.chimpcode.discount.viewmodels.ListPostViewModel
import com.github.clans.fab.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_promo_map.*


class PromoMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {

//    FRAGMENT
    private val TAG : String = "PromoMapFragment *** "
    private var mListener: IFragmentInteractionListener? = null

//    CATEGORIES
    private var fabIds : ArrayList<Int> = ArrayList()

//    LOCATION, MAPS, PERMISSIONS
    private val MY_PERMISSIONS_REQUEST_LOCATION: Int = 101
    private var mFusedLocationClient : FusedLocationProviderClient? = null
    private var mMap : GoogleMap? = null
    private var mLocationPermissionGranted : Boolean = false
    private var mRequestingLocationUpdates : Boolean = false
    private var isCenteredInMyLocation : Boolean = false

//    CURRENT/PREV LOCATION
    private var mlocationCallback : LocationCallback? = null
//    POSITION MARKER - STORE
    private var positionStoreHashMap: HashMap<GLocation, Store>? = null
//    VIEW_MODEL
    private var listPostViewModel: ListPostViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        listPostViewModel = ViewModelProviders.of(activity!!)[ListPostViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promo_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.mapView.onCreate(savedInstanceState)
        view.mapView.onResume() // needed to get the map to display immediately
        view.mapView.getMapAsync(this)

        val categories : Array<GointCategory>? = listPostViewModel!!.allCategoriesDataView
        for (category : GointCategory in categories!!) {
            fabIds.add(category.fabId)
            val fab = view.findViewById<FloatingActionButton>(category.fabId) as FloatingActionButton
            fab.labelText = category.text
            fab.setOnClickListener(this)
            fab.setLabelColors(
                    ContextCompat.getColor(context!!, R.color.gray),
                    ContextCompat.getColor(context!!, R.color.gray_ripple),
                    ContextCompat.getColor(context!!, R.color.gray_ripple)
            )
            fab.setLabelTextColor(
                    ContextCompat.getColor(context!!, R.color.md_black_1000)
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (listPostViewModel != null) {
            listPostViewModel!!.storesByPosition().observe(this, object : Observer<HashMap<GLocation, Store>> {
                override fun onChanged(storesByPosition: HashMap<GLocation, Store>?) {
                    if (storesByPosition != null) {
                        positionStoreHashMap = storesByPosition
                        if (mMap != null) {
                            setUpMarkers(mMap!!, storesByPosition)
                        }
                    }
                }
            })

            listPostViewModel!!.activeCategories().observe(this, object : Observer<List<GointCategory>> {
                override fun onChanged(activeCategories: List<GointCategory>?) {
                    if (activeCategories != null) {
                        updateUI(activeCategories)
                    }
                }
            })
        }
    }

    fun updateUI(activeCategories: List<GointCategory>) {

        val color = ContextCompat.getColor(context!!, activeCategories[0].colorId)
        fab_filter_category_menu.menuButtonColorNormal = color
        mListener!!.onChangeAppColor(color)
    }

    fun setUpMarkers(map : GoogleMap, storesByPosition : HashMap<GLocation, Store>) {

        map.clear()
        for ((location, store) in storesByPosition) {

            var markerType = "default"
            var markerIcon: BitmapDescriptor

            val company = store.postsAssigned[0].by
            if ( company.categories.isNotEmpty() ) {
                markerType = store.postsAssigned[0].by.categories[0].name
            }

            when(markerType) {
                "Productos varios" -> markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.asset_7)
                "Servicios" -> markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.asset_6)
                "Restaurants" -> markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.asset_1)
                "Bares y discos" -> markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.asset_3)
                "Entretenimiento" -> markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.asset_4)
                "Belleza" -> markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.asset_5)
                else -> markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_geo_place)
            }

            val markerOption = MarkerOptions()
                    .position(
                            LatLng(location.latitude.toDouble(), location.longitude.toDouble()))
                    .icon(markerIcon)

            map.addMarker(markerOption)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mRequestingLocationUpdates) {
            startLocationUpdates()
        }
    }

    override fun onStop() {
        super.onStop()
        if(mRequestingLocationUpdates) {
            stopLocationUpdates()
        }
    }

    override fun onClick(view: View) {

        if (fabIds.contains(view.id)) {
            val fab = view as FloatingActionButton
            listPostViewModel!!.switchCategory(fab.labelText)
        }
    }

    override fun onMapReady(mMap: GoogleMap) {
        this.mMap = mMap
        // STYLE MAPS
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style_map))
        mMap.setOnMarkerClickListener(this)

        if (!checkPermission()) {
            mMap.isMyLocationEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false
            requestpermission()
        } else {
            getLastKnowLocation()
            mLocationPermissionGranted = true
            if (!mRequestingLocationUpdates) {
                startLocationUpdates()
            }
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
        }

        if (positionStoreHashMap != null) {
            setUpMarkers(mMap, positionStoreHashMap!!)
        }
    }

    private fun checkPermission() : Boolean {
        return ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestpermission() {
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when(requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
//                    DO SOMETHING AFTER GET LOCATION
                    getLastKnowLocation()
                } else {
                    val requestGpsPermissionAgainSnackbar = Snackbar.make(this.view!!,
                            getString(R.string.request_permission_again),
                            Snackbar.LENGTH_INDEFINITE)

                    requestGpsPermissionAgainSnackbar.setAction("Ok", {
                        _ ->
                        requestGpsPermissionAgainSnackbar.dismiss()
                        requestpermission()
                    }).show()
                }
            }
        }
    }

    private fun getLastKnowLocation() {
        if (checkPermission()) {
            mFusedLocationClient?.lastLocation!!
                    .addOnSuccessListener {
                        location ->
                            if (location != null) {
                                Log.d(TAG, "Last Know Location --->" +
                                        location.latitude.toString()+ "::" +
                                        location.longitude.toString())
                                mMap!!.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                                LatLng(location.latitude, location.longitude),
                                                14f))
                                mMap?.animateCamera(
                                        CameraUpdateFactory.zoomTo(14f))
                                isCenteredInMyLocation = true
                            } else {
                                Log.d(TAG, "Not know location xxxxx")
                            }
                    }
        }
    }

    private fun createLocationRequest() : LocationRequest {
        var mlocationRequest = LocationRequest()

        mlocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mlocationRequest.interval = 1000
        mlocationRequest.fastestInterval= 1000

        return mlocationRequest
    }

    private fun setLocationUpdateSettings(mLocationRequest: LocationRequest) : Task<LocationSettingsResponse>{

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)

        val client : SettingsClient = LocationServices.getSettingsClient(context!!)
        val task : Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        return task
    }

    private fun createLocationCallback () {
        mlocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if(locationResult?.lastLocation != null) {
                    Log.d(TAG, "lastLocation --> " +
                            locationResult.lastLocation.latitude.toString() + " :: " +
                            locationResult.lastLocation.longitude.toString())
                    Log.d(TAG, "location series --> " + locationResult.locations.size.toString())
                    if (listPostViewModel!!.setLocation(locationResult.lastLocation)) {
                    }
                    drawCircle(locationResult.lastLocation, mMap)
                    updateUIonMap(locationResult.lastLocation)
                } else {
                    Log.d(TAG, "lastLocation --> " + " null :: null")
                }
            }
        }
    }

    private fun updateUIonMap(lastLocation: Location) {
        if(mRequestingLocationUpdates) {
            if(!isCenteredInMyLocation) {
                mMap!!.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastLocation.latitude, lastLocation.longitude),
                                14f))
                mMap?.animateCamera(
                        CameraUpdateFactory.zoomTo(14f))
                isCenteredInMyLocation = true
            }
        }
    }

    private fun startLocationUpdates() {
        createLocationCallback()

        val requestLocation = createLocationRequest()
        if (checkPermission()) {
            setLocationUpdateSettings(requestLocation)
                    .addOnSuccessListener {
                        mRequestingLocationUpdates = true
                        mFusedLocationClient?.requestLocationUpdates(requestLocation, mlocationCallback,null)
                    }
                    .addOnFailureListener {
                        e -> e.printStackTrace()
                    }
        }
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient?.removeLocationUpdates(mlocationCallback)!!
                .addOnCompleteListener {
                    task ->
                    mRequestingLocationUpdates = false
                }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement IFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        val store : Store = positionStoreHashMap!![GLocation(latitude = marker.position.latitude.toFloat(),
                longitude = marker.position.longitude.toFloat())]!!

        initAndOpenPromoDialog(store.postsAssigned)
        return true
    }

    private fun initAndOpenPromoDialog (posts : List<GeoPost>) {

        val promoListDialog = PromoGeoItemsDialog(context!!, posts)
        promoListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        promoListDialog.show()
    }

    fun drawCircle(location : Location, map : GoogleMap?) {
        if (map != null) {
            val latlng = LatLng(location.latitude, location.longitude)
            var latLngDiff = listPostViewModel?.latLngDiff?:0.0
            val positionEnd = Location("")
            positionEnd.latitude = location.latitude + latLngDiff
            positionEnd.longitude = location.longitude
            map.addCircle(CircleOptions()
                    .center(latlng)
                    .radius(location.distanceTo(positionEnd).toDouble())
                    .strokeColor(ContextCompat.getColor(context!!, R.color.circleStrokeMapGoint))
                    .fillColor(ContextCompat.getColor(context!!, R.color.circleMapGoint))
            )
        }
    }

}
