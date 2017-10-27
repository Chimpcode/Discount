package com.chimpcode.discount.data

import com.chimpcode.discount.models.Post
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

/**
 * Created by anargu on 9/22/17.
 */
class ApiClient {

    companion object {
        fun getService() : CupointApiService {

            var client : Retrofit = Retrofit.Builder()
                    .baseUrl("http://13.90.253.208:9300/api/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

            var service : CupointApiService = client.create(CupointApiService::class.java)
            return service
        }

    }
}