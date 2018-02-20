package com.chimpcode.discount.data.providers

import android.location.Location
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.chimpcode.discount.common.fromISO8601UTC
import com.chimpcode.discount.data.*

/**
 * Created by anargu on 1/12/18.
 */
fun List<FetchAllPosts.Location>.toLocationModel () : ArrayList<GLocation> {
    val gLocations = ArrayList<GLocation>()
    for (location : FetchAllPosts.Location in this) {
        gLocations.add(
                GLocation(
                        longitude = location.longitude().toFloat(),
                        latitude = location.latitude().toFloat()
                )
        )
    }
    return gLocations
}


fun List<FetchAllPosts.PostsAssigned>.toPostsAssignedModel () : ArrayList<GeoPost> {
    val geoPosts = ArrayList<GeoPost>()
    for (post : FetchAllPosts.PostsAssigned in this) {
        geoPosts.add(
                GeoPost(
                        id = post.id(),
                        title = post.title(),
                        description = post.description() ?: "",
                        image = post.image() ?: "",
                        created_at = fromISO8601UTC(post.createdAt() as String),
                        address = post.address() ?: "",
                        by = post.by().toCompanyModel()
                )
        )
    }
    return geoPosts
}

fun List<FetchAllPosts.LocationByStore>.toStoresModel() : ArrayList<Store> {
    val stores = ArrayList<Store>()
    for (store : FetchAllPosts.LocationByStore in this) {
        stores.add(
                Store(
                        id = store.id(),
                        name = store.name(),
                        GLocations = store.locations()!!.toLocationModel(),
                        postsAssigned = store.postsAssigned()!!.toPostsAssignedModel()
                )
        )
    }
    return stores
}

fun List<FetchAllPosts.Post>.toPostsModel () : ArrayList<Post> {
    val posts = ArrayList<Post>()
    for (post : FetchAllPosts.Post in this) {
        posts.add(
                Post(
                        id = post.id(),
                        title = post.title(),
                        description = post.description() ?: "",
                        image = post.image() ?: "",
                        shows = post.shows(),
                        created_at = fromISO8601UTC(post.createdAt() as String),
                        address = post.address() ?: "",
                        by = post.by().toCompanyModel(),
                        stores = post.locationByStores()!!.toStoresModel()
                )
        )
    }
    return posts
}

fun FetchAllPosts.By.toCompanyModel () : Company {
    return Company(
            id = id(),
            commercialName = commercialName(),
            email = email()
    )
}

fun FetchAllPosts.By1.toCompanyModel () : Company {
    return Company(
            id = id(),
            commercialName = commercialName(),
            email = email(),
            categories = categories()!!.map {
                CompanyCategory(
                        id = it.id(),
                        name = it.name(),
                        tags = it.tags()
                )
            }
    )
}

fun fetchPosts(apolloClient: ApolloClient,
               searchText : String,
               categories : List<String>,
               prevLocation: Location?, latLngDiff: Double?) : ApolloQueryCall<FetchAllPosts.Data> {

    val TAG = "fetchPosts ***"

    var latitude_gt : Double = -90.0
    var latitude_lt : Double = 90.0
    var longitude_gt : Double = -180.0
    var longitude_lt : Double = 180.0

    if (prevLocation != null && latLngDiff != null) {
        latitude_gt = (prevLocation.latitude - latLngDiff)
        latitude_lt = (prevLocation.latitude + latLngDiff)
        longitude_gt = (prevLocation.longitude - latLngDiff)
        longitude_lt = (prevLocation.longitude + latLngDiff)
    }

    val queryParams = FetchAllPosts.builder()
            .search_text(searchText)
            .filterByCategory(categories)
            .latitude_gt(latitude_gt)
            .latitude_lt(latitude_lt)
            .longitude_gt(longitude_gt)
            .longitude_lt(longitude_lt)
            .build()
    val query = apolloClient.query(queryParams)
    return query
}

fun updatePostViews(apolloClient: ApolloClient?, id : String, shows: Int) : Boolean {
    val postAddViewParams = PostAddView.builder()
            .postId(id)
            .shows(shows)
            .build()

    val postAddViewQuery = apolloClient?.mutate(postAddViewParams)
    val response =  postAddViewQuery?.execute()?.data()
    return response != null
}

fun List<CompanyById.Post>.toPostsModel() : List<Post> {

//    posts List<Post>
    return this.map {
        Post(
                id = it.id(),
                title= it.title(),
                created_at = fromISO8601UTC(it.createdAt() as String),
                image = it.image()?:"",
                shows = it.shows(),
                description = it.description()?:""
        )
    }
}

fun CompanyById.Company.toCompanyModel() : Company {
    return Company(
            id = id(),
            commercialName = commercialName(),
            email = email(),
            termsConditions = termsConditions(),
            postsAssigned = posts()!!.toPostsModel()
    )
}


fun getSingleCompanyById(apolloClient: ApolloClient, companyId : String) : ApolloQueryCall<CompanyById.Data> {

    var params = CompanyById.builder()
            .companyId(companyId)
            .build()

    val query = apolloClient.query(params)
    return query
}


fun List<PostById.Location>._toLocationModel () : ArrayList<GLocation> {
    val gLocations = ArrayList<GLocation>()
    for (location : PostById.Location in this) {
        gLocations.add(
                GLocation(
                        longitude = location.longitude().toFloat(),
                        latitude = location.longitude().toFloat()
                )
        )
    }
    return gLocations
}


fun List<PostById.Post1>.__toPostsModel () : ArrayList<Post> {
    val posts = ArrayList<Post>()
    for (_post : PostById.Post1 in this) {
        posts.add(
            Post(
                 id = _post.id(),
                 title = _post.title(),
                 image = _post.image()?:""
            )
        )
    }
    return posts
}


fun List<PostById.LocationByStore>._toStoresModel() : ArrayList<Store> {
    val stores = ArrayList<Store>()
    for (store : PostById.LocationByStore in this) {
        stores.add(
                Store(
                        id = store.id(),
                        name = store.name(),
                        GLocations = store.locations()!!._toLocationModel()
                )
        )
    }
    return stores
}

fun PostById.By.toCompanyModel() : Company {
    return Company(
            id = id(),
            commercialName = commercialName(),
            postsAssigned = posts()!!.__toPostsModel()
    )
}

fun PostById.Post.toPostModel() : Post {
    return Post(
            id = id(),
            title= title(),
            created_at = fromISO8601UTC(createdAt() as String),
            image = image()?:"",
            shows = shows(),
            description = description()?:"",
            by = by().toCompanyModel(),
            stores = locationByStores()!!._toStoresModel()
    )
}

fun fetchSinglePostById(apolloClient: ApolloClient, postId : String) : ApolloQueryCall<PostById.Data> {
    var params = PostById.builder()
            .postId(postId)
            .build()

    val query = apolloClient.query(params)
    return query
}

fun GetUserByFacebookId.User.toUserModel() : User{
    return User(
            id = this.id(),
            fullName = this.fullName(),
            email = this.email(),
            age = this.age().toString(),
            gender = this.gender()
    )
}

fun fetchUser(apolloClient: ApolloClient, facebookId : String) : ApolloQueryCall<GetUserByFacebookId.Data> {

    val params = GetUserByFacebookId.builder()
            .facebookId(facebookId)
            .build()

    val query = apolloClient.query(params)
    return query
}

fun createUser(apolloClient: ApolloClient, user : User) : ApolloMutationCall<CreateUser.Data>{

    val params = CreateUser.builder()
            .fullname(user.fullName)
            .email(user.email)
            .facebookAccount(user.facebookId)
            .username(user.username)
            .password(user.password)
            .build()

    val query = apolloClient.mutate(params)
    return query
}

fun likePromotion(apolloClient: ApolloClient, postId : String, userId : String) : ApolloMutationCall<LikePromotionMutation.Data>{

    var params = LikePromotionMutation.builder()
            .postId(postId)
            .userId(userId)
            .build()
    val query = apolloClient.mutate(params)
    return query
}


fun unLikePromotion(apolloClient: ApolloClient, postId : String, userId : String) : ApolloMutationCall<UnlikePromotionMutation.Data>{

    var params = UnlikePromotionMutation.builder()
            .postId(postId)
            .userId(userId)
            .build()
    val query = apolloClient.mutate(params)
    return query
}


fun MyPromotionsByUser.By._toCompanyModel() : Company {
    return  Company(
            id = id(),
            commercialName = commercialName()
     )
}

fun List<MyPromotionsByUser.MyPromotion>._toPostsModel() : ArrayList<Post> {

    val posts = this.map {
        Post(
                id = it.id(),
                created_at = fromISO8601UTC(it.createdAt() as String),
                title = it.title(),
                description = it.description()?:"",
                image = it.image()?:"",
                shows = it.shows(),
                address = it.address()?:"",
                by = it.by()._toCompanyModel()
        )
    }

    val _posts = ArrayList<Post>()
    _posts.addAll(posts)
    return _posts
}

fun fetchMyPromotions(apolloClient: ApolloClient, userId : String) : ApolloQueryCall<MyPromotionsByUser.Data>{

    var params = MyPromotionsByUser.builder()
            .userId(userId)
            .build()

    val query = apolloClient.query(params)
    return query
}

fun fetchCompanyQuery(apolloClient: ApolloClient, searchText: String) : ApolloQueryCall<CompanyQuery.Data> {

    var params = CompanyQuery.builder()
            .company_name(searchText)
            .build()

    val query = apolloClient.query(params)
    return query
}


fun addSubscription(apolloClient: ApolloClient, companyId: String, userId: String) : ApolloMutationCall<AddToCompanySubscription.Data> {

    var params = AddToCompanySubscription.builder()
            .companyId(companyId)
            .userId(userId)
            .build()

    val query = apolloClient.mutate(params)
    return query
}

fun removeSubscription(apolloClient: ApolloClient, companyId: String, userId: String) : ApolloMutationCall<RemoveFromCompanySubscription.Data> {

    var params = RemoveFromCompanySubscription.builder()
            .companyId(companyId)
            .userId(userId)
            .build()

    val query = apolloClient.mutate(params)
    return query
}


fun fetchMySubscriptions(apolloClient: ApolloClient, userId: String) : ApolloQueryCall<MySubscriptionsByUser.Data> {

    var params = MySubscriptionsByUser.builder()
            .userId(userId)
            .build()

    val query = apolloClient.query(params)
    return query
}