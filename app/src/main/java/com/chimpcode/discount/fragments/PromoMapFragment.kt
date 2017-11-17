package com.chimpcode.discount.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chimpcode.discount.R
import com.google.android.gms.maps.*
import android.support.v4.app.ActivityCompat
import android.content.DialogInterface
import android.location.Location
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Window
import com.chimpcode.discount.data.ApiClient
import com.chimpcode.discount.models.MarkerData
import com.chimpcode.discount.ui.views.MarkerInfoView
import com.chimpcode.discount.ui.views.PromoGeoItemsDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_promo_map.view.*
import org.jetbrains.annotations.NotNull


class PromoMapFragment : Fragment(), OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleMap.OnMarkerClickListener {



    val TAG : String = "PromoMapFragment"

    private var mFusedLocationClient : FusedLocationProviderClient? = null
    private var mMap : GoogleMap? = null
    private var mGoogleApiClient : GoogleApiClient? = null
    private var mLocationPermissionGranted : Boolean = false
    private var mListener: IFragmentInteractionListener? = null
    private val MY_PERMISSIONS_REQUEST_LOCATION: Int = 101
    private var mLastKnownLocation : Location? = null
    private var mDefaultLocation : LatLng = LatLng(-11.892479, -77.046922)
    private var locationRequest : LocationRequest = LocationRequest.create()

    private val allMarkersMap : MutableMap<Marker, List<MarkerData>> = HashMap<Marker, List<MarkerData>>()

    companion object {
        fun newInstance(): PromoMapFragment {
            val fragment = PromoMapFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000
        locationRequest.fastestInterval= 1000

        mGoogleApiClient = GoogleApiClient.Builder(context)
                .enableAutoManage(activity/* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build()
        mGoogleApiClient?.connect()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_promo_map, container, false)
        rootView.mapView.onCreate(savedInstanceState)
        rootView.mapView.onResume() // needed to get the map to display immediately
        rootView.mapView.getMapAsync(this)
        return rootView
    }

    override fun onMapReady(mMap: GoogleMap) {
        this.mMap = mMap

//        val markerInfoView = MarkerInfoView(activity, allMarkersMap)
        mMap.setOnMarkerClickListener(this)
//        mMap.setInfoWindowAdapter(markerInfoView)
//        mMap.setOnInfoWindowClickListener(markerInfoView)
        getLocationPermission()
        updateLocationUI()
        getDeviceLocation()
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when(requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (mLocationPermissionGranted) {
                mMap!!.isMyLocationEnabled = true
                mMap!!.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap!!.isMyLocationEnabled = false
                mMap!!.uiSettings.isMyLocationButtonEnabled = false

                mLastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException)  {
            Log.e("Exception: %s", e.message)
        }
    }

    private fun getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
        }
        // A step later in the tutorial adds the code to get the device location.

        try {
            Log.d("getDeviceLocation ::: ", "mLocationPermissionGranted : " + mLocationPermissionGranted.toString())
            if (mLocationPermissionGranted) {
                Log.d("PROMOMAP ::: ", "location permision granted true")
                val locationResult : Task<Location> = mFusedLocationClient!!.lastLocation
                locationResult.addOnCompleteListener(activity, object : OnCompleteListener<Location> {
                    override fun onComplete(@NotNull task: Task<Location>) {
                        if (task.isSuccessful) {
                            mLastKnownLocation = task.result
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude),
                                    14f))
                            mMap?.animateCamera(CameraUpdateFactory.zoomTo(14f))
                            val marker = mMap?.addMarker(MarkerOptions()
                                    .position(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_geo_place))
                                    //.title("TIENDAS EL: 30% descuento en ROPAS"))
                            )

                            val promos : MutableList<MarkerData> = ArrayList<MarkerData>()
                            promos.add(MarkerData("1","TIENDAS EL: 30% descuento en ROPAS", "disponible"))
                            promos.add(MarkerData("1","TIENDAS EL: 40% descuento en ZAPATOS", "disponible"))

                            allMarkersMap.put(marker!!, promos)

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.")
                            Log.e(TAG, "Exception: %s", task.getException())
                            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 14f))
                            mMap?.animateCamera(CameraUpdateFactory.zoomTo(14f))
                            mMap?.addMarker(MarkerOptions()
                                    .position(mDefaultLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_geo_place))
                                    .title("I'm Here! (Offline)"))
                            mMap!!.uiSettings.isMyLocationButtonEnabled = false
                        }
                    }
                })

            }
        } catch (e : SecurityException) {
            Log.e("Exception: %s", e.message)
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

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnected(p0: Bundle?) {
//        try {
//            mFusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this)
//        } catch (e : SecurityException) {
//            Log.e("Exception: %s", e.message)
//        }
    }

    override fun onLocationChanged(location: Location?) {
        Log.i("onLocation Changed: %s", location.toString())

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val promos = allMarkersMap[marker]!!
        Log.d(activity.localClassName.toString(), promos.toString())

        initAndOpenPromoDialog(promos)
        return true
    }

    private fun initAndOpenPromoDialog (promos : List<MarkerData>) {

        val promoListDialog = PromoGeoItemsDialog(context, promos)
        promoListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        promoListDialog.show()
    }
}
