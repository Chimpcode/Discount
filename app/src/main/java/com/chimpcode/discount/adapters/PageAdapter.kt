package com.chimpcode.discount.adapters

import android.os.Debug
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import com.chimpcode.discount.fragments.PromoListFragment
import com.chimpcode.discount.fragments.PromoMapFragment

/**
 * Created by anargu on 9/1/17.
 */
class PageAdapter(fm: FragmentManager, internal var mNumOfTabs: Int) : FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                val tab1 = PromoListFragment()
                return tab1
            }
            1 -> {
                val tab2 = PromoMapFragment()
                return tab2
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position) {
            0 -> {
                return "Cerca a m\u00ed"
            }
            1 -> {
                return "Mapa"
            }
            else -> {
                return "NONE"
            }
        }
    }

}