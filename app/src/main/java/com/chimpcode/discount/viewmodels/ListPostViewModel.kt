package com.chimpcode.discount.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.chimpcode.discount.R
import com.chimpcode.discount.data.*
import com.chimpcode.discount.data.providers.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by anargu on 2/2/18.
 */
class ListPostViewModel : ViewModel() {

    val TAG = "ListPostViewModel ***"
    var apolloClient : ApolloClient? = null

    var selectedCategories = MutableLiveData<List<GointCategory>>()

    val allCategoriesDataView = arrayOf(
            GointCategory(text = "Todos",
                    value = listOf<String>("Productos varios", "Servicios", "Restaurants", "Bares y discos", "Entretenimiento", "Belleza"),
                    colorId = R.color.allColor, color = "#C12B35", fabId = R.id.fab_all),
            GointCategory(text = "Productos varios",
                    value = listOf("Productos varios"),
                    colorId = R.color.variosColor, color = "#2b94c1", fabId = R.id.fab_varios),
            GointCategory(text = "Servicios",
                    value = listOf("Servicios"),
                    colorId = R.color.serviciosColor, color = "#00ACCD", fabId = R.id.fab_servicios),
            GointCategory(text = "Restaurants",
                    value = listOf("Restaurants"), colorId = R.color.restaurantsColor, color = "#E7632C", fabId = R.id.fab_restaurant),
            GointCategory(text = "Bares y discos",
                    value = listOf("Bares y discos"), colorId = R.color.baresColor, color = "#77378D", fabId = R.id.fab_bares),
            GointCategory(text = "Entretenimiento",
                    value = listOf("Entretenimiento"), colorId = R.color.entretenimientoColor, color = "#E9B400", fabId = R.id.fab_entretenimiento),
            GointCategory(text = "Belleza",
                    value = listOf("Belleza"), colorId = R.color.bellezaColor, color = "#5DAF52", fabId = R.id.fab_belleza)
    )

    val defaultCategory = allCategoriesDataView.findByText("Todos")!!

//    var backupPosts : ArrayList<Post>? = null
    var mutablePosts : MutableLiveData<List<Post>>? = null
    var mutableStoresByLocation : MutableLiveData<HashMap<GLocation, Store>>? = null
    var mutableMyPosts :  MutableLiveData<List<Post>>? = null

//    LOCATION
    var currentLocation : Location? = null
    val rateLatLngDiff : Double = 0.02
    var latLngDiff : Double? = null

    init {
        selectedCategories.postValue(listOf(defaultCategory))
    }

    fun settingApolloClient (_apolloClient: ApolloClient) {
        apolloClient = _apolloClient
    }

    fun switchCategory(categoryText : String) {
        val category = allCategoriesDataView.findByText(categoryText)!!
        selectedCategories.value = (listOf(category))
        fetchPosts()
    }

    fun fetchPosts(searchText : String = "") {
        var categories = listOf<String>()
        if (selectedCategories.value != null) {
            if (selectedCategories.value!!.isNotEmpty()) {
                categories = selectedCategories.value!![0].value
            }
        }

        fetchPosts(apolloClient!!, searchText, categories,
                currentLocation, latLngDiff).enqueue(
                object : ApolloCall.Callback<FetchAllPosts.Data>() {
                    override fun onFailure(e: ApolloException) {
                        Log.d(TAG, e.message)
                    }
                    override fun onResponse(response: Response<FetchAllPosts.Data>) {
                        val posts = response.data()!!.posts().toPostsModel()

                        updateData(posts)
                    }
                }
        )
    }

    fun fetchMyPosts(userId : String) {
        if (mutableMyPosts == null) {
             mutableMyPosts = MutableLiveData()
        }
        fetchMyPromotions(apolloClient!!, userId).enqueue(object : ApolloCall.Callback<MyPromotionsByUser.Data>() {

            override fun onFailure(e: ApolloException) {
                Log.d(TAG, e.message)
            }

            override fun onResponse(response: Response<MyPromotionsByUser.Data>) {
                val rawUser = response.data()!!.user()
                val posts : ArrayList<Post>
                posts = if (rawUser == null) {
                    ArrayList()
                } else {
                    rawUser.myPromotions()!!._toPostsModel()
                }

                updateMyPostsData(posts)
            }
        })
    }

    fun myposts(): MutableLiveData<List<Post>> {
        if (mutableMyPosts == null) {
            mutableMyPosts = MutableLiveData()
        }
        return mutableMyPosts!!
    }

    fun updateMyPostsData(posts: ArrayList<Post>) {
        mutableMyPosts!!.postValue(posts)
    }

    fun updateData(posts : List<Post>) {
        doAsync {
            val stores = ArrayList<Store>()
            val storesByPosition = HashMap<GLocation, Store>()
            for ( _post : Post in posts ) {
                stores.addAll(_post.stores)
            }
            for (_store : Store in stores) {
                for (_glocation : GLocation in _store.GLocations) {
                    storesByPosition[_glocation] = _store
                }
            }
            if (currentLocation != null) {
                posts.map {
                    val loc = Location("")
                    loc.longitude = it.stores[0].GLocations[0].longitude.toDouble()
                    loc.latitude = it.stores[0].GLocations[0].latitude.toDouble()

                    it.distance = currentLocation!!.distanceTo(loc)
                }
            }
            onComplete {
                mutablePosts!!.postValue(posts)
                mutableStoresByLocation!!.postValue(storesByPosition)
            }
        }
    }

    fun setRateLatLng(rate : Int) {
        latLngDiff = rateLatLngDiff * rate
    }

    fun filterPostByText(newText: String) {

        if (mutablePosts!!.value != null && newText != "") {

            fetchPosts(searchText = newText)

//            BACKUP
//            if (backupPosts == null) {
//                backupPosts = ArrayList()
//                backupPosts!!.addAll(mutablePosts!!.value!!)
//            }
//
//            val filteredPosts = ArrayList<Post>()
//            for (_post : Post in mutablePosts!!.value!!.iterator()) {
//                if (_post.title.contains(newText)) {
//                    filteredPosts.add(_post)
//                }
//            }
//            mutablePosts!!.postValue(filteredPosts)
        }
    }

    fun setLocation (loc : Location) : Boolean {
        var isSet = false
        if (currentLocation != null) {
            val latDiff = (currentLocation as Location).latitude - loc.latitude
            val lngDiff = (currentLocation as Location).longitude - loc.longitude
            Log.d(TAG, "location diffs --> $latDiff :: $lngDiff")
            if ( latDiff > 0.0001 || lngDiff > 0.0001) {
                currentLocation = loc
                fetchPosts()
                isSet = true
            }
        } else {
            currentLocation = loc
            fetchPosts()
        }
        return isSet
    }

    fun posts () : MutableLiveData<List<Post>> {
        if (mutablePosts == null) {
            mutablePosts = MutableLiveData()
//            fetchPosts()
        }
        return mutablePosts!!
    }

    fun storesByPosition () : MutableLiveData<HashMap<GLocation, Store>> {
        if (mutableStoresByLocation == null) {
            mutableStoresByLocation = MutableLiveData()
//            fetchPosts()
        }
        return mutableStoresByLocation!!
    }

    fun activeCategories() : MutableLiveData<List<GointCategory>> {
        return selectedCategories
    }

    fun likePost(postId : String, userId : String) {

        likePromotion(apolloClient!!, postId = postId, userId = userId).enqueue(object : ApolloCall.Callback<LikePromotionMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d(TAG, e.message)
            }

            override fun onResponse(response: Response<LikePromotionMutation.Data>) {
                Log.d(TAG, response.toString())
                Log.d(TAG, "UsersLiked")
//                response.data()!!.response()!!.usersLikedUser()!!.id()
            }

        })
    }

    override fun onCleared() {
    }
}