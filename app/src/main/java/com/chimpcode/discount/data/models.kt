package com.chimpcode.discount.data

import com.chimpcode.discount.common.GeoPromoConstants
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.common.PromoViewType
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

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

data class GeoPostMinimized (
        val id : String = "",
        val by: String = "",
        val title: String = "",
        val GLocation: GLocation = GLocation(0f, 0f),
        val stock: Int = 99999
): Serializable

data class GLocation(
        val longitude : Float,
        val latitude : Float
) : Serializable

data class Store (
        val id : String,
        val name : String,
        val GLocations: List<GLocation> = ArrayList<GLocation>(),
        val postsAssigned : List<GeoPost> = ArrayList<GeoPost>()
) : Serializable

data class Company (
        val commercialName : String = "",
        val email : String = "",
        val logoUrl: String = "",
        val id: String = "",
        val termsConditions : String? = null,
        val postsAssigned: List<Post> = ArrayList<Post>(),
        val categories: List<CompanyCategory> = ArrayList<CompanyCategory>(),
        var isSubscribed : Boolean = false
) : Serializable

data class CompanyCategory (
        val id : String = "",
        val name : String = "",
        val alias: String = "",
        val tags: List<String>? = null
) : Serializable


data class Post (
        val id : String = "",
        val by : Company = Company(commercialName = "", email = "", id = ""),
        val created_at : Date? = null,
        val title : String = "",
        var image : String = "",
        val description : String = "",
        val address : String = "",
        var shows: Int = 0,
        val stores : List<Store> = ArrayList<Store>(),
        val stock : Int = 0,
        var distance : Float? = null
) : Serializable, PromoViewType {
    override fun getViewType(): Int = PromoConstants.NULL
}

data class GeoPost (
        val id : String = "",
        val by : Company = Company(commercialName = "", email = "", id = ""),
        val created_at : Date? = null,
        val title : String = "",
        var image : String = "",
        val description : String = "",
        val address : String = "",
        val stores : List<Store> = ArrayList<Store>(),
        val stock : Int = 0
) : Serializable, PromoViewType {
    override fun getViewType(): Int = GeoPromoConstants.ITEM
}

data class GointCategory(
        val id : String = "A",
        val value: List<String> = ArrayList<String>(),
        val text : String = "---",
        val colorId: Int,
        val color: String = "#C12B35",
        val fabId: Int
) : Serializable

data class User(
        val id : String = "",
        val fullName : String = "",
        val email : String = "",
        val age : String = "",
        val facebookId : String = "",
        val gender : String? = null,
        val myPromotions: ArrayList<Post> = ArrayList(),
        val username : String = "",
        val password : String = ""
)

// extensions
fun Array<GointCategory>.findByText(text : String) : GointCategory? {
    return this.firstOrNull { it.text == text }
}