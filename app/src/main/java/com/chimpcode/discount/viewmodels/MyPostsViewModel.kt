package com.chimpcode.discount.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.chimpcode.discount.data.Post
import com.chimpcode.discount.data.providers.*

/**
 * Created by anargu on 2/3/18.
 */
class MyPostsViewModel : ViewModel() {

    val TAG = "MyPostsViewModel ***"
    var _myposts : MutableLiveData<List<Post>>? = null
    var userId : String = ""
    var apolloClient : ApolloClient? = null

    fun myposts() : MutableLiveData<List<Post>> {
        if (_myposts == null) {
            _myposts = MutableLiveData()
        }
        return  _myposts!!
    }

    fun fetchMyPosts() {
        fetchMyPromotions(apolloClient!!, userId).enqueue(object : ApolloCall.Callback<MyPromotionsByUser.Data>() {

            override fun onFailure(e: ApolloException) {
                Log.d(TAG, e.message)
            }

            override fun onResponse(response: Response<MyPromotionsByUser.Data>) {
                val rawUser = response.data()!!.user()
                val posts : ArrayList<Post> = rawUser!!.myPromotions()!!._toPostsModel()

                updateData(posts)
            }


        })
    }

    fun updateData(myPromotions: ArrayList<Post>) {
        _myposts!!.postValue(myPromotions)
    }

    fun unlikePost(userId : String, postId : String) {
        unLikePromotion(apolloClient!!, postId = postId, userId = userId).enqueue(object : ApolloCall.Callback<UnlikePromotionMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d(TAG, e.message)
            }

            override fun onResponse(response: Response<UnlikePromotionMutation.Data>) {
                Log.d(TAG, response.toString())
                Log.d(TAG, "UsersLiked")
            }
        })
    }

    override fun onCleared() {

    }
}