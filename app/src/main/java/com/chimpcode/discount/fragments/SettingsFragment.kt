package com.chimpcode.discount.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.chimpcode.discount.R

/**
 * Created by anargu on 10/19/17.
 */
class SettingsFragment() : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_general)
    }

}