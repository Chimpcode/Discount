package com.chimpcode.discount.data.retrofit

import com.chimpcode.discount.data.GeoPostMinimized
import com.chimpcode.discount.data.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by anargu on 9/22/17.
 */
interface CupointApiService {

    @GET("p")
    fun listPosts() : Call<Map<String, Post>>

    @GET("p/{post_id}")
    fun getPost(@Path("post_id") postId : String ) : Call<Map<String, Post>>

    @GET("m/nearby")
    fun getNearPromotions(@Query("lon") lon : Float, @Query("lat") lat: Float, @Query("radio") radio: Float ) : Call<Map<String, GeoPostMinimized>>

}