package com.chimpcode.discount.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chimpcode.discount.R
import com.chimpcode.discount.adapters.PromoAdapter
import kotlinx.android.synthetic.main.fragment_promo_list.view.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PromoListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PromoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PromoListFragment : Fragment() {

    private var mListener: IFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragmentView = inflater!!.inflate(R.layout.fragment_promo_list, container, false)
        val elements = Array<String> (12, {a -> a.toString()})

        val mAdapter = PromoAdapter(this@PromoListFragment.context, elements)
        val mLayoutManager : RecyclerView.LayoutManager= GridLayoutManager(this@PromoListFragment.context, 1)

        fragmentView.recycler.layoutManager = mLayoutManager
        fragmentView.recycler.itemAnimator = DefaultItemAnimator()
        fragmentView.recycler.adapter = mAdapter


        return fragmentView
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */

    companion object {
        fun newInstance(): PromoListFragment {
            return PromoListFragment()
        }
    }
}// Required empty public constructor
