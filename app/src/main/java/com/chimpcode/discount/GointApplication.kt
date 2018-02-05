package com.chimpcode.discount

import android.app.Application
import android.content.Context
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import com.apollographql.apollo.CustomTypeAdapter
import com.chimpcode.discount.common.fromISO8601UTC
import com.chimpcode.discount.common.toISO8601UTC
import type.CustomType
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by anargu on 1/10/18.
 */
class GointApplication : Application() {

    var apolloClient : ApolloClient? = null
//    http://13.90.253.208:60000/simple/v1/cjb5vhjxf002q01897ggfz3b3 OLD
//    http://13.90.253.208:60000/simple/v1/cjcae1ay000en0189jqrz4n2q
    val BASE_URL = "http://13.90.253.208:60000/simple/v1/cjcae1ay000en0189jqrz4n2q"

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
                .build()
        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
//                .addCustomTypeAdapter(CustomType.DATETIME ,customTypeAdapter)
                .build()


    }

    fun apolloClient() : ApolloClient? {
        return apolloClient
    }
}