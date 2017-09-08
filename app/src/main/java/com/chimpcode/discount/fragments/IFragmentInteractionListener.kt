package com.chimpcode.discount.fragments

import android.net.Uri

/**
 * Created by anargu on 9/3/17.
 */
interface IFragmentInteractionListener {

    fun onFragmentInteraction(TAG : String, uri : Uri)
}