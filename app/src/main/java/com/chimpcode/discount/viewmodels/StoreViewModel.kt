package com.chimpcode.discount.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.chimpcode.discount.data.Company
import com.chimpcode.discount.data.providers.getSingleCompanyById
import com.chimpcode.discount.data.providers.toCompanyModel

/**
 * Created by anargu on 2/3/18.
 */
class StoreViewModel : ViewModel() {

    val TAG = "StoreViewModel ***"

    var companyId : String? = null
    var _store: MutableLiveData<Company>? = null
    var apolloClient : ApolloClient? = null

    fun store () : MutableLiveData<Company> {
        if (_store == null) {
            _store = MutableLiveData()
        }
        return _store!!
    }

    fun settingApolloClient (_apolloClient: ApolloClient) {
        apolloClient = _apolloClient
    }

    fun companyId (id : String) {
        companyId = id
    }

    fun fetchCompanyById() {
        if (companyId != null) {
            getSingleCompanyById(apolloClient!!, companyId!!).enqueue(object : ApolloCall.Callback<CompanyById.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.d(TAG, e.message)
                }
                override fun onResponse(response: Response<CompanyById.Data>) {
                    val company : Company = response.data()!!.Company()!!.toCompanyModel()
                    updateData(company)
                }
            })
        }
    }

    fun updateData(company: Company) {
        _store!!.postValue(company)
    }

    override fun onCleared() {
    }
}