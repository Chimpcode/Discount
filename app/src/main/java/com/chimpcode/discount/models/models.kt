package com.chimpcode.discount.models

import com.chimpcode.discount.common.GeoPromoConstants
import com.chimpcode.discount.common.PromoViewType

/**
 * Created by anargu on 11/13/17.
 */
data class MarkerData (
        var id : String,
        var label: String,
        var status: String
) : PromoViewType {
    override fun getViewType() = GeoPromoConstants.ITEM
}

