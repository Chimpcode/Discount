package com.chimpcode.discount.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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