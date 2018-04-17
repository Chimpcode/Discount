package com.chimpcode.discount.viewmodels

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.chimpcode.discount.data.User
import com.chimpcode.discount.data.providers.createUser
import com.chimpcode.discount.data.providers.fetchUser
import com.chimpcode.discount.data.providers.toUserModel
import org.json.JSONObject

/**
 * Created by anargu on 2/4/18.
 */
class LoginViewModel : ViewModel() {

    val TAG = "LoginViewModel *** "
    var facebookId = ""
    var userId = ""
    var apolloClient : ApolloClient? = null

    fun login(apolloClient: ApolloClient, facebookId: String, interactor: LoginViewInteractor, jsonobject: JSONObject) {
         fetchUser(apolloClient, facebookId).enqueue(object : ApolloCall.Callback<GetUserByFacebookId.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d(TAG, e.message)
                interactor.onFailedLogin(e.message?:"Error. Try again.")
            }
            override fun onResponse(response: Response<GetUserByFacebookId.Data>) {
//                if null save a new user if not, user info obtained
                var user: User?
                if (response.data()!!.users().isEmpty()) {
//                    craete an user
//                    jsonobject
                    val username = (jsonobject["email"] as String).replace('@', '.')
                    val password = (jsonobject["email"] as String).replace('@', '.')
                    user = User(
                            fullName = jsonobject["name"] as String,
                            facebookId = jsonobject["id"] as String,
                            email = jsonobject["email"] as String,
                            gender = jsonobject["gender"] as String,
                            username = username,
                            password = password
                    )
                    createUser(apolloClient, user).enqueue(object : ApolloCall.Callback<CreateUser.Data>() {
                        override fun onFailure(e: ApolloException) {

                        }

                        override fun onResponse(response: Response<CreateUser.Data>) {

                            val user = User(
                                    id = response.data()!!.user()!!.id(),
                                    email = response.data()!!.user()!!.email(),
                                    facebookId = response.data()!!.user()!!.facebookAccount()!!,
                                    fullName = response.data()!!.user()!!.fullName(),
                                    username = response.data()!!.user()!!.username(),
                                    password = response.data()!!.user()!!.password()
                            )
                            interactor.onCompleteLogin(user)
                        }

                    })
                } else {
//                    get user already in system
                    user = response.data()!!.users()[0].toUserModel()
                    interactor.onCompleteLogin(user)
                }
            }

        })
    }

    override fun onCleared() {

    }
}
interface LoginViewInteractor {

    fun onCompleteLogin(user: User)
    fun onFailedLogin(message: String)
}