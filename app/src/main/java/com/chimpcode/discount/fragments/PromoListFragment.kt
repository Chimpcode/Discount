package com.chimpcode.discount.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chimpcode.discount.R
import com.chimpcode.discount.adapters.PromoAdapter
import com.chimpcode.discount.common.PromoConstants
import com.chimpcode.discount.data.ApiClient
import com.chimpcode.discount.models.Post
import com.chimpcode.discount.models.PromLocation
import kotlinx.android.synthetic.main.fragment_promo_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromoListFragment : Fragment() {

    private var mListener: IFragmentInteractionListener? = null
    val elements = ArrayList<Post> ()
    private val mAdapter: PromoAdapter by lazy {
        PromoAdapter(context, elements, PromoConstants.CARD)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragmentView = inflater!!.inflate(R.layout.fragment_promo_list, container, false)

        val mLayoutManager : RecyclerView.LayoutManager= GridLayoutManager(context, 1)

        fragmentView.recycler.layoutManager = mLayoutManager
        fragmentView.recycler.itemAnimator = DefaultItemAnimator()
        fragmentView.recycler.adapter = mAdapter

        return fragmentView
    }

    fun fetchPosts() {

        ApiClient.getService().listPosts().enqueue(object : Callback<Map<String, Post>> {
            override fun onResponse(call: Call<Map<String, Post>>?, response: Response<Map<String, Post>>?) {
                Log.d("HOME ACT","::: response ::: ")
                if (response!!.isSuccessful) {
                    Log.d("HOME ACT",response.body().toString())

                    for ((label, post) in response.body()!!) {
                        post.image = "http://13.90.253.208:9300/api/i/" + post.image
                        mAdapter.insertSingleData(post)
                    }
                }
                mAdapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Map<String, Post>>?, t: Throwable?) {
                Log.d("HOME ACT","::: error ::: ")
            }
        })
    }

    override fun onStart() {
        super.onStart()

//        fetchPosts()
        mAdapter.fillSampleData()
    }

    override fun onResume() {
        super.onResume()

        Log.d("ON RESUME", "size: "+ elements.size)
        if (elements.size == 0) {
//            fetchPosts()
            mAdapter.fillSampleData()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement IFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {
        fun newInstance(): PromoListFragment {
            return PromoListFragment()
        }
    }

}

