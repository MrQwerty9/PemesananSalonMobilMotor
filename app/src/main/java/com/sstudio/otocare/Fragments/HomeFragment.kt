package com.sstudio.otocare.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.sstudio.otocare.BookingActivity
import com.sstudio.otocare.Model.Banner

import com.sstudio.otocare.R
import com.sstudio.otocare.adapter.HomeSliderAdapter
import com.sstudio.otocare.adapter.LookbookAdapter
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.listener.IBannerLoadListener
import com.sstudio.otocare.listener.ILookbookLoadListener
import com.sstudio.otocare.services.PicassoImageLoadingServices
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import ss.com.bannerslider.Slider

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment(), IBannerLoadListener, ILookbookLoadListener {

    var bannerRef: CollectionReference = FirebaseFirestore.getInstance().collection("Banner")
    var lookbookRef: CollectionReference = FirebaseFirestore.getInstance().collection("LookBook")
    lateinit var iBannerLoadListener: IBannerLoadListener
    lateinit var iLookbookLoadListener: ILookbookLoadListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        Slider.init(PicassoImageLoadingServices())
        iBannerLoadListener = this
        iLookbookLoadListener = this

        if (true) {
            setUserInformation(view)
            loadBanner()
            loadLookbook()
        }
        view.cv_booking.setOnClickListener {
            startActivity(Intent(activity, BookingActivity::class.java))
        }
        return view
    }

    private fun loadLookbook() {
        lookbookRef.get()
            .addOnCompleteListener { task ->
                var lookbooks: ArrayList<Banner> = ArrayList<Banner>()
                if (task.isSuccessful) {
                    for (bannerSnapshoot: QueryDocumentSnapshot in task.result!!) {
                        val banner = bannerSnapshoot.toObject(Banner::class.java)
                        lookbooks.add(banner)
                    }
                    iLookbookLoadListener.onLookbookLoadSuccess(lookbooks)
                }
            }.addOnFailureListener { e ->
                iLookbookLoadListener.onLookbookLoadFailed(e.message!!)
            }
    }

    private fun loadBanner() {
        bannerRef.get()
            .addOnCompleteListener { task ->
                var banners: ArrayList<Banner> = ArrayList<Banner>()
                if (task.isSuccessful) {
                    for (bannerSnapshoot: QueryDocumentSnapshot in task.result!!) {
                        val banner = bannerSnapshoot.toObject(Banner::class.java)
                        banners.add(banner)
                    }
                    iBannerLoadListener.onBannerLoadSuccess(banners)
                }
            }.addOnFailureListener { e ->
                iBannerLoadListener.onBannerLoadFailed(e.message!!)
            }
    }

    private fun setUserInformation(view: View) {

        view.layout_user_information.visibility = View.VISIBLE
        view.tv_user_name.text = Common.currentUser?.nama

    }

    override fun onLookbookLoadSuccess(banners: List<Banner>) {
        rv_lookbook.setHasFixedSize(true)
        rv_lookbook.layoutManager = LinearLayoutManager(activity)
        rv_lookbook.adapter = LookbookAdapter(requireActivity(), banners)
    }

    override fun onLookbookLoadFailed(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBannerLoadSuccess(banner: List<Banner>) {
        banner_slider_home.setAdapter(HomeSliderAdapter(banner))
    }

    override fun onBannerLoadFailed(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

}
