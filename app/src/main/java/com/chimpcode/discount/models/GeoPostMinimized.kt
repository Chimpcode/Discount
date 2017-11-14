package com.chimpcode.discount.models

import java.io.Serializable

/**
 * Created by anargu on 9/25/17.
 */
data class GeoPostMinimized (
        val id : String = "",
        val by: String = "",
        val title: String = "",
        val location: PromLocation = PromLocation(0f,0f),
        val stock: Int = 99999
 ): Serializable