package com.sstudio.pemesanansalonmobilmotor.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.accountkit.AccountKit

import com.sstudio.pemesanansalonmobilmotor.R
import com.sstudio.pemesanansalonmobilmotor.common.Common
import com.sstudio.pemesanansalonmobilmotor.services.PicassoImageLoadingServices
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import ss.com.bannerslider.Slider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        Slider.init( PicassoImageLoadingServices())

        if(AccountKit.getCurrentAccessToken() != null){
            setUserInformation(view)
        }
        return view
    }

    private fun setUserInformation(view: View) {

        view.layout_user_information.visibility = View.VISIBLE
        view.tv_user_name.text = Common.currentUser!!.nama
    }


}
