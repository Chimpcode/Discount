package com.chimpcode.discount.data.providers

import android.util.Log
import com.github.billybichon.livegql.LiveGQLListener
import com.github.billybichon.livegql.LiveGQL
import com.github.billybichon.livegql.Subscription


/**
 * Created by anargu on 2/1/18.
 */
class LiveGQLClient {

    val TAG = "LiveRepository *** "
    val url = "ws://13.90.253.208:60000/subscriptions/v1/cjcae1ay000en0189jqrz4n2q"
    private val liveGQL : LiveGQL
    private var subscription : Subscription? = null

    val queryExample = "" +
            "subscription {\n" +
            "      Post {\n" +
            "        mutation\n" +
            "        node {\n" +
            "          address\n" +
            "          id\n" +
            "          title\n" +
            "          stock\n" +
            "        }\n" +
            "      }\n" +
            "}"

    init {
        liveGQL = LiveGQL(url, object : LiveGQLListener {
            override fun onConnectionClose() {
                Log.d(TAG, "onConnectionClosed")
            }
            override fun onError(error: String) {
                Log.d(TAG, "onError")
                Log.d(TAG, "===> $error")
            }
            override fun onMessageReceived(message: String, tag: String) {
                Log.d(TAG, "onMessage received message: $message tag: $tag")
            }
            override fun onConnectionOpen() {
                Log.d(TAG, "onConnectionOpen")
            }

        })
    }

    fun initializeSubscription () {
        Log.d(TAG, "INITIALIZE GRAPH SUBSCRIPTION")
        subscription = liveGQL.subscribe(queryExample, "SOMETAG")
    }

    fun shutdown() {
        if (subscription != null) {
            liveGQL.unsubscribe(subscription)
            liveGQL.closeConnection()
        }
    }
}

