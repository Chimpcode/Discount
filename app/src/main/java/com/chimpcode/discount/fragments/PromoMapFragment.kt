package com.chimpcode.discount.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chimpcode.discount.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PromoMapFragment.IFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PromoMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PromoMapFragment : Fragment(), OnMapReadyCallback {

    val TAG : String = "PromoMapFragment"
    private var mListener: IFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_promo_map, container, false)

        return rootView
    }


    override fun onMapReady(p0: GoogleMap?) {
        val map = p0

        // Add a marker in Sydney, Australia, and move the camera.
        val sydney = LatLng(-34.0, 151.0)
        map?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment PromoMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): PromoMapFragment {
            val fragment = PromoMapFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
