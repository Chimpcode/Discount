package com.chimpcode.discount.models

import java.io.Serializable

/**
 * Created by anargu on 9/22/17.
 */
data class Post (
//        val PostId : String,
//
//        val By : String = "",
//        val CreatedAt : String = "",
//        val Title : String = "",
//        val Image : String = "",
//        val Description : String = "",
//        val Address : String = "",
//        val Distance : Int = 0,
//        val Stock : Int = 0

    val id : String = "",
    val by : String = "",
    val created_at : String = "",
    val title : String = "",
    var image : String = "",
    val description : String = "",
    val address : String = "",
    val location : PromLocation,
    val stock : Int = 0
) : Serializable