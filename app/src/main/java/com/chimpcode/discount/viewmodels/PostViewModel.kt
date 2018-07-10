package com.chimpcode.discount.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.chimpcode.discount.data.Post
import com.chimpcode.discount.data.providers.fetchSinglePostById
import com.chimpcode.discount.data.providers.toPostModel
import com.chimpcode.discount.data.providers.updatePostViews

/**
 * Created by anargu on 2/3/18.
 */
class PostViewModel : ViewModel() {

    val TAG = "PostViewModel ***"

    var postId : String? = null
    var _post: MutableLiveData<Post>? = null
    var apolloClient : ApolloClient? = null

    fun settingApolloClient (_apolloClient: ApolloClient) {
        apolloClient = _apolloClient
    }

    fun postId (id : String) {
        postId  = id
    }

    fun updateViewsPost(post: Post) {
        updatePostViews(apolloClient, post.id, post.shows + 1)
    }

    fun fetchSinglePost() {
        fetchSinglePostById(apolloClient!!, postId!!).enqueue(object : ApolloCall.Callback<PostById.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d(TAG, e.message)
            }

            override fun onResponse(response: Response<PostById.Data>) {
                val post = response.data()!!.post()!!.toPostModel()
                updateData(post)
                updateViewsPost(post)
            }
        })
    }

    fun updateData(post: Post) {
        _post!!.postValue(post)
    }

    fun post() : MutableLiveData<Post> {
        if (_post == null) {
            _post = MutableLiveData()
        }
        return _post!!
    }

    override fun onCleared() {

    }
}