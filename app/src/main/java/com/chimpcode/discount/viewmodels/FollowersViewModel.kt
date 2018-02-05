package com.chimpcode.discount.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.chimpcode.discount.data.Company
import com.chimpcode.discount.data.providers.addSubscription
import com.chimpcode.discount.data.providers.fetchCompanyQuery
import com.chimpcode.discount.data.providers.fetchMySubscriptions
import com.chimpcode.discount.data.providers.removeSubscription

/**
 * Created by anargu on 2/5/18.
 */
class FollowersViewModel : ViewModel() {

    val TAG = "FollowersViewModel ***"
    var _companies : MutableLiveData<List<Company>>? = null
    var apolloClient : ApolloClient? = null
    var userId : String? = null

//    request
    private fun fetchCompanies() {
        val searchText = ""
        if (apolloClient != null) {
            fetchCompanyQuery(apolloClient!!, searchText).enqueue(object : ApolloCall.Callback<CompanyQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.d(TAG, e.message)
                }
                override fun onResponse(response: Response<CompanyQuery.Data>) {
                    val companies : List<Company> = response.data()!!.companies().map {
                        Company(
                                id = it.id(),
                                commercialName = it.commercialName(),
                                logoUrl = it.logoImage()
                        )
                    }
                    if (_companies == null) {
                        _companies = MutableLiveData()
                    }
//                    _companies!!.postValue(companies)
                    if (userId != null) {
                        fetchMySubscriptions(userId!!, companies)
                    }
                }
            })
        }
    }

    fun fetchMySubscriptions(userId: String, companies : List<Company>) {
        fetchMySubscriptions(apolloClient!!, userId).enqueue(object: ApolloCall.Callback<MySubscriptionsByUser.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d(TAG, e.message)
            }

            override fun onResponse(response: Response<MySubscriptionsByUser.Data>) {
                val companiesSubscribed = response.data()!!.user()!!.companiesFollowing()!!.map {
                    Company(
                            id = it.id(),
                            commercialName = it.commercialName(),
                            logoUrl = it.logoImage()
                    )
                }

                for (__company : Company in companies) {
                    for (companySubscribed : Company in companiesSubscribed) {
                        __company.isSubscribed = __company.id == companySubscribed.id
                    }
                }
                _companies!!.postValue(companies)
            }

        })
    }

    fun subscribeToCompany(companyId: String, userId: String) {
        addSubscription(apolloClient!!, companyId = companyId, userId = userId).enqueue(object : ApolloCall.Callback<AddToCompanySubscription.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d(TAG, e.message)
            }
            override fun onResponse(response: Response<AddToCompanySubscription.Data>) {
                Log.d(TAG, response.toString())
            }
        })
    }

    fun unsubscribeFromCompany(companyId: String, userId: String) {
        removeSubscription(apolloClient!!, companyId = companyId, userId = userId).enqueue(object : ApolloCall.Callback<RemoveFromCompanySubscription.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d(TAG, e.message)
            }
            override fun onResponse(response: Response<RemoveFromCompanySubscription.Data>) {
                Log.d(TAG, response.toString())
            }
        })
    }


    fun companies() : MutableLiveData<List<Company>> {
        if (_companies== null) {
            _companies = MutableLiveData()
        }
        fetchCompanies()
        return _companies!!
    }
}
